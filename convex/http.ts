import { httpRouter } from "convex/server";

import { api } from "./_generated/api";
import { httpAction } from "./_generated/server";
import { BillingPlan, getGenerationLimit, getSubscriptionEndForType, SubscriptionType } from "./subscriptions";

const ALLOWED_ORIGINS = ["https://homedecor.ai", "http://localhost:8081"];

function isAllowedOrigin(origin: string | null): boolean {
  if (!origin) return true;
  return ALLOWED_ORIGINS.some((allowed) => origin === allowed || origin.startsWith(`${allowed}/`));
}

const http = httpRouter();

function normalizeHaystack(values: Array<string | null | undefined>) {
  return values.filter(Boolean).join(" ").toLowerCase();
}

function parseTimestamp(value: unknown) {
  if (typeof value === "number" && Number.isFinite(value)) {
    return value;
  }
  if (typeof value === "string") {
    const asNumber = Number(value);
    if (Number.isFinite(asNumber)) {
      return asNumber;
    }
    const parsed = Date.parse(value);
    if (Number.isFinite(parsed)) {
      return parsed;
    }
  }
  return null;
}

function inferSubscriptionType(event: any): SubscriptionType {
  const haystack = normalizeHaystack([
    event?.product_id,
    event?.product_identifier,
    event?.store_product_id,
    event?.offering_id,
    ...(Array.isArray(event?.entitlement_ids) ? event.entitlement_ids : []),
  ]);

  if (haystack.includes("year") || haystack.includes("annual")) {
    return "yearly";
  }
  if (haystack.includes("week")) {
    return "weekly";
  }
  return "free";
}

function inferPlan(event: any): BillingPlan {
  const periodType = String(event?.period_type ?? event?.periodType ?? "").toLowerCase();
  if (periodType === "trial" || periodType === "intro") {
    return "trial";
  }
  return "pro";
}

function rejectIfDisallowedOrigin(request: Request): Response | null {
  const origin = request.headers.get("origin");
  if (!isAllowedOrigin(origin)) {
    return new Response("Forbidden", { status: 403 });
  }
  return null;
}

http.route({
  path: "/webhooks/revenuecat",
  method: "POST",
  handler: httpAction(async (ctx, request) => {
    const blocked = rejectIfDisallowedOrigin(request);
    if (blocked) return blocked;

    const expectedAuth = process.env.REVENUECAT_WEBHOOK_AUTH;
    const authorization = request.headers.get("authorization");
    if (expectedAuth && authorization !== expectedAuth && authorization !== `Bearer ${expectedAuth}`) {
      return new Response("Unauthorized", { status: 401 });
    }

    const payload = await request.json();
    const event = payload?.event ?? payload;
    const clerkId = event?.app_user_id ?? event?.appUserId ?? event?.aliases?.[0];
    if (typeof clerkId !== "string" || clerkId.trim().length === 0) {
      return new Response(JSON.stringify({ ok: false, error: "Missing app_user_id" }), { status: 400 });
    }

    const eventType = String(event?.type ?? "").toUpperCase();
    const now = Date.now();
    const expirationAt = parseTimestamp(event?.expiration_at_ms) ?? parseTimestamp(event?.expiration_at);

    if (eventType === "EXPIRATION") {
      await ctx.runMutation(api.users.syncRevenueCatSubscriptionInternal, {
        clerkId,
        plan: "free",
        subscriptionType: "free",
        purchasedAt: now,
        subscriptionEnd: 0,
        internalToken: process.env.CONVEX_INTERNAL_API_TOKEN,
      });
      return Response.json({ ok: true, clerkId, subscriptionType: "free" });
    }

    const subscriptionType = inferSubscriptionType(event);
    if (subscriptionType === "free") {
      return new Response(JSON.stringify({ ok: false, error: "Unknown subscription type" }), { status: 400 });
    }

    const purchasedAt = parseTimestamp(event?.purchased_at_ms)
      ?? parseTimestamp(event?.purchased_at)
      ?? parseTimestamp(event?.original_purchase_at_ms)
      ?? parseTimestamp(event?.original_purchase_at)
      ?? now;
    const subscriptionEnd = expirationAt ?? getSubscriptionEndForType(subscriptionType, purchasedAt);
    const plan = inferPlan(event);

    await ctx.runMutation(api.users.syncRevenueCatSubscriptionInternal, {
      clerkId,
      plan,
      subscriptionType,
      purchasedAt,
      subscriptionEnd,
      internalToken: process.env.CONVEX_INTERNAL_API_TOKEN,
    });

    return Response.json({
      ok: true,
      clerkId,
      plan,
      subscriptionType,
      subscriptionEnd,
      imageLimit: getGenerationLimit(subscriptionType),
    });
  }),
});

http.route({
  path: "/webhooks/revenuecat/refund",
  method: "POST",
  handler: httpAction(async (ctx, request) => {
    const blocked = rejectIfDisallowedOrigin(request);
    if (blocked) return blocked;

    const expectedAuth = process.env.REVENUECAT_WEBHOOK_AUTH;
    const authorization = request.headers.get("authorization");
    if (expectedAuth && authorization !== expectedAuth && authorization !== `Bearer ${expectedAuth}`) {
      return new Response("Unauthorized", { status: 401 });
    }

    const payload = await request.json();
    const event = payload?.event ?? payload;
    const clerkId = event?.app_user_id ?? event?.appUserId ?? event?.aliases?.[0];
    if (typeof clerkId !== "string" || clerkId.trim().length === 0) {
      return new Response(JSON.stringify({ ok: false, error: "Missing app_user_id" }), { status: 400 });
    }

    const eventType = String(event?.type ?? "").toUpperCase();

    if (eventType !== "REFUND" && eventType !== "UNCANCELLATION") {
      return new Response(JSON.stringify({ ok: true, skipped: true }), { status: 200 });
    }

    const affectedStore = String(event?.store ?? "").toLowerCase();

    if (affectedStore === "app_store") {
      const originalTransactionId = String(event?.original_transaction_id ?? event?.transaction_id ?? "").trim();
      if (!originalTransactionId) {
        return new Response(JSON.stringify({ ok: false, error: "Missing transaction_id" }), { status: 400 });
      }

      const generation = await ctx.runQuery(api.generations.getGenerationByOrderId, {
        orderId: originalTransactionId,
      });

      if (generation && generation.status === "failed" && !generation.diamondRefunded) {
        await ctx.runMutation(api.users.fulfillDiamondPurchase, {
          clerkId,
          packId: "starter",
          orderId: originalTransactionId,
          receiptData: JSON.stringify(event),
          internalToken: process.env.CONVEX_INTERNAL_API_TOKEN,
        });

        await ctx.runMutation(api.generations.markGenerationRefunded, {
          id: generation._id,
          internalToken: process.env.CONVEX_INTERNAL_API_TOKEN,
        });

        return Response.json({ ok: true, action: "refund_processed", clerkId });
      }

      return Response.json({ ok: true, action: "no_action_needed", clerkId });
    }

    if (affectedStore === "play_store") {
      const productIds = Array.isArray(event?.product_ids) ? event.product_ids : [];
      const diamondProduct = productIds.find((id: string) =>
        id.includes("starter") || id.includes("designer") || id.includes("creator") ||
        id.includes("pro") || id.includes("studio") || id.includes("architect") || id.includes("estate"),
      );

      if (diamondProduct) {
        return Response.json({ ok: true, action: "play_store_refund_detected", clerkId });
      }

      return Response.json({ ok: true, action: "subscription_refund", clerkId });
    }

    return Response.json({ ok: true, action: "unknown_store", clerkId });
  }),
});

export default http;
