import { chromium } from "playwright";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";

(async () => {
  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const mCtx = await browser.newContext({
    viewport: { width: 375, height: 812 },
    deviceScaleFactor: 3,
    isMobile: true,
    hasTouch: true,
  });
  const m = await mCtx.newPage();

  await m.goto(`${BASE_URL}/tools`, { waitUntil: "domcontentloaded", timeout: 30000 });

  // Wait for canvas (Compose rendering)
  try { await m.waitForSelector("canvas", { timeout: 15000 }); } catch {}
  await m.waitForTimeout(8000);

  // Try to wait for "Preparing" text to disappear
  try {
    await m.waitForFunction(() => {
      return !document.body.innerText.includes("Preparing your design studio");
    }, { timeout: 15000 });
  } catch {}
  await m.waitForTimeout(3000);

  // Dump ALL text on page
  const bodyText = await m.evaluate(() => document.body.innerText);
  console.log("=== Body text ===");
  console.log(bodyText.substring(0, 2000));

  // Dump all interactive elements
  const elements = await m.evaluate(() => {
    const results = [];
    const all = document.querySelectorAll("button, a, [role='button'], [onclick]");
    all.forEach(el => {
      const text = el.innerText?.trim().substring(0, 100);
      const tag = el.tagName;
      const role = el.getAttribute("role");
      const rect = el.getBoundingClientRect();
      if (text || rect.width > 50) {
        results.push({ tag, role, text: text || "(no text)", x: Math.round(rect.x), y: Math.round(rect.y), w: Math.round(rect.width), h: Math.round(rect.height) });
      }
    });
    return results;
  });

  console.log("\n=== Interactive elements ===");
  elements.forEach((el, i) => {
    console.log(`${i}: <${el.tag}> role=${el.role} at (${el.x},${el.y}) ${el.w}x${el.h} text="${el.text}"`);
  });

  // Take a screenshot for visual reference
  await m.screenshot({ path: "screenshots/debug_mobile_tools.png", fullPage: true });
  console.log("\n[Screenshot saved] debug_mobile_tools.png");

  await mCtx.close();
  await browser.close();
})();
