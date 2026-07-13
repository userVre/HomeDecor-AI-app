import { chromium } from "playwright";
import { writeFileSync } from "fs";
import { join } from "path";
import { readdirSync } from "fs";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";
const SHOT_DIR = join(process.cwd(), "screenshots");

let idx = 82;
const errors = [];

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
  try { await page.waitForFunction(() => !document.body.innerText.includes("Preparing your design studio"), { timeout: 15000 }); } catch {}
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

const MOBILE_TOOLS = [
  { name: "interior",  scrollY: 0,    btnY: 530 },
  { name: "facade",    scrollY: 0,    btnY: 940 },
  { name: "garden",    scrollY: 400,  btnY: 530 },
  { name: "paint",     scrollY: 800,  btnY: 530 },
  { name: "floor",     scrollY: 1200, btnY: 530 },
  { name: "layout",    scrollY: 1600, btnY: 530 },
  { name: "replace",   scrollY: 2000, btnY: 530 },
  { name: "reference", scrollY: 2400, btnY: 530 },
];

async function mobileWizardFlow(page, tool) {
  const t = MOBILE_TOOLS.find(m => m.name === tool);
  console.log(`\n[MOBILE WIZARD] ${tool}`);
  await go(page, "/tools");
  await page.evaluate(y => window.scrollTo(0, y), t.scrollY);
  await page.waitForTimeout(600);
  await tapAt(page, 150, t.btnY, `card_${tool}`);
  await page.waitForTimeout(3000);
  if (page.url().includes("/tools")) {
    console.log(`  [RETRY] tapping card image...`);
    await page.evaluate(y => window.scrollTo(0, y), t.scrollY);
    await page.waitForTimeout(400);
    await tapAt(page, 185, t.btnY - 180, `card_img_${tool}`);
    await page.waitForTimeout(3000);
  }
  await shot(page, `mobile_wizard_${tool}_01_landing`);
  await tapAt(page, 185, 500, "see_it_in_action");
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${tool}_02_example`);
  for (let step = 3; step <= 10; step++) {
    await tapAt(page, 300, 770, `next_step${step}`);
    await page.waitForTimeout(2500);
    await shot(page, `mobile_wizard_${tool}_step${step}`);
    const pageText = await page.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      console.log(`  [DONE] ${tool} reached Generate at step ${step}`);
      await shot(page, `mobile_wizard_${tool}_generate`);
      break;
    }
  }
}

async function paywallFlow(page) {
  console.log("\n[PAYWALL]");
  await go(page, "/pro");
  await clickAt(page, 700, 860, "get_pro");
  await page.waitForTimeout(2000);
  await shot(page, "paywall_01_sheet");
  for (let step = 2; step <= 5; step++) {
    await clickAt(page, 750, 770, `paywall_continue_${step}`);
    await page.waitForTimeout(1500);
    await shot(page, `paywall_0${step}_step${step}`);
  }
  await clickAt(page, 365, 107, "close_paywall");
  await page.waitForTimeout(1000);
}

async function settingsFlow(page) {
  console.log("\n[SETTINGS]");
  await go(page, "/profile");
  await clickAt(page, 1300, 100, "gear_icon");
  await page.waitForTimeout(2000);
  await shot(page, "settings_01_main");
  await page.mouse.wheel(0, 300);
  await page.waitForTimeout(1000);
  await shot(page, "settings_02_scrolled");
  await clickAt(page, 700, 400, "language");
  await page.waitForTimeout(1500);
  await shot(page, "settings_03_language");
  await clickAt(page, 700, 700, "close_language");
  await page.waitForTimeout(1000);
  await clickAt(page, 700, 470, "theme");
  await page.waitForTimeout(1000);
  await shot(page, "settings_04_theme");
  await clickAt(page, 700, 470, "theme_back");
  await page.waitForTimeout(800);
  await clickAt(page, 700, 600, "feedback");
  await page.waitForTimeout(1500);
  await shot(page, "settings_05_feedback");
  await clickAt(page, 600, 650, "cancel_feedback");
  await page.waitForTimeout(800);
  await page.mouse.wheel(0, 400);
  await page.waitForTimeout(800);
  await clickAt(page, 700, 700, "logout");
  await page.waitForTimeout(1500);
  await shot(page, "settings_06_logout");
  await clickAt(page, 600, 650, "cancel_logout");
  await page.waitForTimeout(800);
  await clickAt(page, 50, 30, "close_settings");
  await page.waitForTimeout(1000);
}

async function authFlow(page) {
  console.log("\n[AUTH]");
  await go(page, "/profile");
  await clickAt(page, 340, 475, "get_started");
  await page.waitForTimeout(3000);
  await shot(page, "auth_01_signin");
  await clickAt(page, 750, 650, "switch_to_signup");
  await page.waitForTimeout(2000);
  await shot(page, "auth_02_signup");
  await clickAt(page, 50, 30, "close_auth");
  await page.waitForTimeout(1000);
}

(async () => {
  console.log(`\n=== Screenshot Continuation (from #${idx + 1}) ===`);
  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });

  // ─── DESKTOP CONTINUATION ───
  console.log("\n═══ DESKTOP (continued) ═══");
  const dCtx = await browser.newContext({ viewport: { width: 1440, height: 900 }, deviceScaleFactor: 2 });
  const d = await dCtx.newPage();
  d.on("console", m => { if (m.type() === "error") errors.push(m.text()); });
  d.on("pageerror", e => errors.push(e.message));

  // Continue replace wizard from step6
  console.log("\n[DESKTOP WIZARD replace - continued]");
  await go(d, "/tools");
  await clickAt(d, 1060, 520, "card_replace");
  await d.waitForTimeout(3000);
  await clickAt(d, 730, 520, "see_it_in_action");
  await d.waitForTimeout(3000);
  // Skip to step6 (we already have up to step5)
  for (let step = 3; step <= 5; step++) {
    await clickAt(d, 1300, 855, `skip_step${step}`);
    await d.waitForTimeout(1500);
  }
  for (let step = 6; step <= 10; step++) {
    await clickAt(d, 1300, 855, `next_step${step}`);
    await d.waitForTimeout(2500);
    await shot(d, `desktop_wizard_replace_step${step}`);
    const pageText = await d.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      await shot(d, "desktop_wizard_replace_generate");
      break;
    }
  }

  // Reference wizard
  console.log("\n[DESKTOP WIZARD reference]");
  await go(d, "/tools");
  await clickAt(d, 1430, 520, "card_reference");
  await d.waitForTimeout(3000);
  await shot(d, "desktop_wizard_reference_01_landing");
  await clickAt(d, 730, 520, "see_it_in_action");
  await d.waitForTimeout(3000);
  await shot(d, "desktop_wizard_reference_02_example");
  for (let step = 3; step <= 10; step++) {
    await clickAt(d, 1300, 855, `next_step${step}`);
    await d.waitForTimeout(2500);
    await shot(d, `desktop_wizard_reference_step${step}`);
    const pageText = await d.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      await shot(d, "desktop_wizard_reference_generate");
      break;
    }
  }

  // Paywall
  await paywallFlow(d);

  // Settings
  await settingsFlow(d);

  // Auth
  await authFlow(d);

  await dCtx.close();

  // ─── MOBILE ───
  console.log("\n\n═══ MOBILE ═══");
  const mCtx = await browser.newContext({
    viewport: { width: 375, height: 812 },
    deviceScaleFactor: 3,
    isMobile: true,
    hasTouch: true,
  });
  const m = await mCtx.newPage();
  m.on("console", tm => { if (tm.type() === "error") errors.push(tm.text()); });
  m.on("pageerror", e => errors.push(e.message));

  // Mobile main pages
  for (const [name, path] of [["tools", "/tools"], ["discover", "/discover"], ["board", "/board"], ["pro", "/pro"], ["profile", "/profile"]]) {
    await go(m, path);
    await shot(m, `mobile_page_${name}`);
  }

  // Mobile bottom nav
  console.log("\n[MOBILE NAV]");
  await go(m, "/tools");
  await shot(m, "mobile_nav_tools");
  await tapAt(m, 75, 780, "nav_discover"); await shot(m, "mobile_nav_discover");
  await tapAt(m, 225, 780, "nav_board"); await shot(m, "mobile_nav_board");
  await tapAt(m, 375, 780, "nav_pro"); await shot(m, "mobile_nav_pro");
  await tapAt(m, 525, 780, "nav_profile"); await shot(m, "mobile_nav_profile");

  // Mobile settings
  console.log("\n[MOBILE SETTINGS]");
  await tapAt(m, 350, 100, "gear_icon");
  await m.waitForTimeout(2000); await shot(m, "mobile_settings");

  // Mobile all8 wizard flows
  console.log("\n[MOBILE WIZARDS]");
  for (const tool of ["interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference"]) {
    await mobileWizardFlow(m, tool);
  }

  await mCtx.close();

  if (errors.length > 0) {
    writeFileSync(join(SHOT_DIR, "console-errors.txt"), errors.join("\n"));
    console.log(`\n[ERRORS] ${errors.length} errors saved`);
  }

  await browser.close();
  console.log(`\n=== DONE! ${idx} total screenshots ===\n`);
})();
