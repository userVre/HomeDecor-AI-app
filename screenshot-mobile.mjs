import { chromium } from "playwright";
import { mkdirSync, existsSync } from "fs";
import { join } from "path";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";
const SHOT_DIR = join(process.cwd(), "screenshots");
if (!existsSync(SHOT_DIR)) mkdirSync(SHOT_DIR, { recursive: true });

let idx = 119;

async function shot(page, label) {
  idx++;
  const slug = label.replace(/[^a-zA-Z0-9_-]/g, "_").slice(0, 80);
  const p = join(SHOT_DIR, `${String(idx).padStart(3, "0")}_${slug}.png`);
  await page.screenshot({ path: p, fullPage: true });
  console.log(`  [SHOT] ${p}`);
}

async function waitApp(page) {
  try { await page.waitForSelector("canvas", { timeout: 15000 }); } catch {}
  await page.waitForTimeout(4000);
  try {
    await page.waitForFunction(() => {
      return !document.body.innerText.includes("Preparing your design studio");
    }, { timeout: 15000 });
  } catch {}
  await page.waitForTimeout(1500);
}

async function go(page, path) {
  await page.goto(`${BASE_URL}${path}`, { waitUntil: "domcontentloaded", timeout: 30000 });
  await waitApp(page);
}

async function clickAt(page, x, y, label) {
  await page.mouse.click(x, y);
  await page.waitForTimeout(1500);
  console.log(`  [CLICK] ${label || `(${x},${y})`}`);
}

async function tapAt(page, x, y, label) {
  await page.touchscreen.tap(x, y);
  await page.waitForTimeout(1500);
  console.log(`  [TAP] ${label || `(${x},${y})`}`);
}

const TOOL_CARDS = {
  interior:  { x: 310, y: 305 },
  facade:    { x: 680, y: 305 },
  garden:    { x: 1060, y: 305 },
  paint:     { x: 1430, y: 305 },
  floor:     { x: 310, y: 520 },
  layout:    { x: 680, y: 520 },
  replace:   { x: 1060, y: 520 },
  reference: { x: 1430, y: 520 },
};

async function mobileWizardFlow(page, tool) {
  console.log(`\n  [MOBILE_WIZARD] ${tool}`);
  await go(page, "/tools");

  const card = TOOL_CARDS[tool];
  await tapAt(page, card.x, card.y, `tool_card_${tool}`);
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${tool}_01_landing`);

  await tapAt(page, 730, 520, "try_example_btn");
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${tool}_02_example`);

  for (let step = 3; step <= 10; step++) {
    await tapAt(page, 350, 855, `next_step${step}`);
    await page.waitForTimeout(2500);
    await shot(page, `mobile_wizard_${tool}_step${step}`);

    const pageText = await page.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      console.log(`  [MOBILE_WIZARD] ${tool} reached Generate at step ${step}`);
      await shot(page, `mobile_wizard_${tool}_generate`);
      break;
    }
  }
  await go(page, "/tools");
}

(async () => {
  console.log(`\n=== Mobile Screenshot Continuation (from #${idx + 1}) ===`);

  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const mCtx = await browser.newContext({ viewport: { width: 375, height: 812 }, deviceScaleFactor: 3, isMobile: true, hasTouch: true });
  const m = await mCtx.newPage();

  // Mobile bottom nav
  console.log("\n═══ Mobile Bottom Nav ═══");
  await go(m, "/tools");
  await shot(m, "mobile_01_tools");
  await tapAt(m, 75, 780, "nav_discover"); await shot(m, "mobile_02_discover");
  await tapAt(m, 225, 780, "nav_board"); await shot(m, "mobile_03_board");
  await tapAt(m, 375, 780, "nav_pro"); await shot(m, "mobile_04_pro");
  await tapAt(m, 525, 780, "nav_profile"); await shot(m, "mobile_05_profile");

  // Mobile settings
  console.log("\n═══ Mobile Settings ═══");
  await tapAt(m, 350, 100, "gear_icon");
  await m.waitForTimeout(2000);
  await shot(m, "mobile_06_settings");

  // Mobile wizards
  console.log("\n═══ Mobile Wizards ═══");
  for (const tool of ["interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference"]) {
    await mobileWizardFlow(m, tool);
  }

  await mCtx.close();
  await browser.close();
  console.log(`\n=== DONE! ${idx} total screenshots ===\n`);
})();
