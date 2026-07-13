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
  await m.waitForTimeout(5000);

  // Dump all clickable elements with text
  const elements = await m.evaluate(() => {
    const results = [];
    const all = document.querySelectorAll("button, a, [role='button'], [onclick], [class*='card'], [class*='Card']");
    all.forEach(el => {
      const text = el.innerText?.trim().substring(0, 80);
      const tag = el.tagName;
      const role = el.getAttribute("role");
      const cls = el.className?.substring?.(0, 60) || "";
      if (text) results.push({ tag, role, cls, text });
    });
    return results;
  });

  console.log("=== Clickable elements on mobile tools page ===");
  elements.forEach((el, i) => {
    console.log(`${i}: <${el.tag}> role=${el.role} class="${el.cls}" text="${el.text}"`);
  });

  // Also check for elements with "Redesign room" text
  const redesign = await m.evaluate(() => {
    const all = document.querySelectorAll("*");
    const results = [];
    for (const el of all) {
      if (el.innerText?.includes("Redesign room") && el.children.length === 0) {
        const rect = el.getBoundingClientRect();
        results.push({
          tag: el.tagName,
          text: el.innerText.substring(0, 80),
          x: rect.x, y: rect.y, w: rect.width, h: rect.height,
        });
      }
    }
    return results;
  });

  console.log("\n=== 'Redesign room' elements ===");
  redesign.forEach((el, i) => {
    console.log(`${i}: <${el.tag}> at (${el.x},${el.y}) ${el.w}x${el.h} text="${el.text}"`);
  });

  // Check all tool card titles
  const titles = await m.evaluate(() => {
    const results = [];
    for (const el of document.querySelectorAll("h2, h3, [class*='title'], [class*='name']")) {
      const text = el.innerText?.trim();
      if (text && text.length < 50) results.push(text);
    }
    return results;
  });

  console.log("\n=== Tool titles ===");
  titles.forEach(t => console.log(`  ${t}`));

  await mCtx.close();
  await browser.close();
})();
