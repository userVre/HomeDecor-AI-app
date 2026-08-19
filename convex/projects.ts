import { mutationGeneric, queryGeneric } from "convex/server";
import { v } from "convex/values";
import { resolveViewer } from "./viewer";

export const list = queryGeneric({
  args: {
    anonymousId: v.optional(v.string()),
  },
  handler: async (ctx, args) => {
    const viewer = await resolveViewer(ctx, { anonymousId: args.anonymousId });
    if (!viewer) return [];

    return await ctx.db
      .query("projects")
      .withIndex("by_userId", (q) => q.eq("userId", viewer.userId))
      .order("desc")
      .collect();
  },
});

export const create = mutationGeneric({
  args: {
    name: v.string(),
    anonymousId: v.optional(v.string()),
  },
  handler: async (ctx, args) => {
    const viewer = await resolveViewer(ctx, { anonymousId: args.anonymousId });
    if (!viewer) throw new Error("Unauthorized");

    const name = args.name.trim();
    if (name.length < 2) {
      throw new Error("Project name is too short.");
    }

    const id = await ctx.db.insert("projects", {
      userId: viewer.userId,
      name,
      createdAt: Date.now(),
    });

    return { id };
  },
});
