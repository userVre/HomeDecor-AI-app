import { chromium } from "playwright";
import { mkdirSync, existsSync, readdirSync } from "fs";
import { join } from "path";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";
const SHOT_DIR = join(process.cwd(), "screenshots");

let idx = Math.max(...readdirSync(SHOT_DIR).filter(f => f.endsWith(".png")).map(f => parseInt(f.split("_")[0])));

async function shot(page, label) {
  idx++;
  const slug = label.replace(/[^a-zA-Z0-9_-]/g, "_").slice(0, 80);
  const p = join(SHOT_DIR, `${String(idx).padStart(3, "0")}_${slug}.png`);
  await page.screenshot({ path: p, fullPage: true });
  console.log(`  [SHOT] ${p}`);
}

async function go(page, path) {
  await page.goto(`${BASE_URL}${path}`, { waitUntil: "domcontentloaded", timeout: 30000 });
  try { await page.waitForSelector("canvas", { timeout: 15000 }); } catch {}
  await page.waitForTimeout(5000);
  try {
    await page.waitForFunction(() => !document.body.innerText.includes("Preparing your design studio"), { timeout: 15000 });
  } catch {}
  await page.waitForTimeout(1500);
}

async function tapAt(page, x, y, label) {
  await page.touchscreen.tap(x, y);
  await page.waitForTimeout(1500);
  console.log(`  [TAP] ${label || `(${x},${y})`}`);
}

// Mobile tools page card positions (from debug screenshot, 375x812 viewport):
// Interior Design:   "Redesign room" button at (~150, 530)
// Exterior Design:   "Restyle exterior" button at (~150, 940)
// Garden Design:     after scrolling ~400px, button at (~150, 530 relative to viewport)
// Smart Wall Paint:  after scrolling ~800px
// Floor Design:      after scrolling ~1200px
// Layout Makeover:   after scrolling ~1600px
// Replace Furniture: after scrolling ~2000px
// Reference Style:   after scrolling ~2400px

const MOBILE_TOOLS = [
  { name: "interior", scrollY: 0, btnY: 530 },
  { name: "facade",   scrollY: 0, btnY: 940 },
  { name: "garden",   scrollY: 400, btnY: 530 },
  { name: "paint",    scrollY: 800, btnY: 530 },
  { name: "floor",    scrollY: 1200, btnY: 530 },
  { name: "layout",   scrollY: 1600, btnY: 530 },
  { name: "replace",  scrollY: 2000, btnY: 530 },
  { name: "reference",scrollY: 2400, btnY: 530 },
];

async function mobileWizardFlow(page, toolName) {
  const tool = MOBILE_TOOLS.find(t => t.name === toolName);
  console.log(`\n  [MOBILE_WIZARD] ${toolName}`);
  await go(page, "/tools");

  // Scroll to the card
  await page.evaluate(y => window.scrollTo(0, y), tool.scrollY);
  await page.waitForTimeout(800);

  // Tap the CTA button
  await tapAt(page, 150, tool.btnY, `${toolName}_cta`);
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${toolName}_01_landing`);

  // Check if we actually left the tools page
  const url = page.url();
  if (url.includes("/tools")) {
    console.log(`  [WARN] Still on tools page, trying to tap card image instead...`);
    // Tap the card image area instead (center of card)
    await page.evaluate(y => window.scrollTo(0, y), tool.scrollY);
    await page.waitForTimeout(500);
    await tapAt(page, 185, tool.btnY - 150, `${toolName}_card_image`);
    await page.waitForTimeout(3000);
    const url2 = page.url();
    if (url2.includes("/tools")) {
      console.log(`  [ERROR] Still on tools page after card tap, skipping ${toolName}`);
      return;
    }
    await shot(page, `mobile_wizard_${toolName}_01_landing`);
  }

  // Click "See it in action"
  await page.waitForTimeout(1000);
  // The button should be visible on the wizard page
  await tapAt(page, 185, 500, "see_it_in_action");
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${toolName}_02_example`);

  // Click Next through wizard steps
  for (let step = 3; step <= 10; step++) {
    await tapAt(page, 300, 770, `next_step${step}`);
    await page.waitForTimeout(2500);
    await shot(page, `mobile_wizard_${toolName}_step${step}`);

    const pageText = await page.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      console.log(`  [DONE] ${toolName} reached Generate at step ${step}`);
      await shot(page, `mobile_wizard_${toolName}_generate`);
      break;
    }
  }
  await go(page, "/tools");
}

(async () => {
  console.log(`\n=== Mobile Wizard Retry v2 (from #${idx + 1}) ===`);

  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const mCtx = await browser.newContext({
    viewport: { width: 375, height: 812 },
    deviceScaleFactor: 3,
    isMobile: true,
    hasTouch: true,
  });
  const m = await mCtx.newPage();

  // First, let me verify the scroll positions by checking each card
  console.log("\n=== Verifying card positions ===");
  await go(m, "/tools");

  for (const tool of MOBILE_TOOLS) {
    await m.evaluate(y => window.scrollTo(0, y), tool.scrollY);
    await m.waitForTimeout(500);
    await m.screenshot({ path: `screenshots/debug_scroll_${tool.name}.png`, fullPage: false });
    console.log(`  [DEBUG] ${tool.name} at scrollY=${tool.scrollY}`);
  }

  // Now do the actual wizards
  console.log("\n=== Running mobile wizards ===");
  const FAILED = ["facade", "garden", "paint", "layout", "replace", "reference"];
  for (const tool of FAILED) {
    await mobileWizardFlow(m, tool);
  }

  await mCtx.close();
  await browser.close();
  console.log(`\n=== DONE! ${idx} total screenshots ===\n`);
})();
