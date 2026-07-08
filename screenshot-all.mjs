import { chromium } from "playwright";
import { mkdirSync, existsSync, writeFileSync } from "fs";
import { join } from "path";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";
const SHOT_DIR = join(process.cwd(), "screenshots");
if (!existsSync(SHOT_DIR)) mkdirSync(SHOT_DIR, { recursive: true });

let idx = 0;
const errors = [];

async function shot(page, label) {
  idx++;
  const slug = label.replace(/[^a-zA-Z0-9_-]/g, "_").slice(0, 80);
  const p = join(SHOT_DIR, `${String(idx).padStart(3, "0")}_${slug}.png`);
  await page.screenshot({ path: p, fullPage: true });
  console.log(`  [SHOT] ${p}`);
}

function sl(s) { return s.replace(/[^a-zA-Z0-9]/g, "_").slice(0, 60); }

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

// ═══════════════════════════════════════════════════
// COORDINATE MAP (1440x900 viewport, 2x DPR = 2880px internal)
// Coordinates are in CSS pixels (half of device pixels)
// ═══════════════════════════════════════════════════

// Nav bar items (centered in top bar)
const NAV = {
  tools:    { x: 275, y: 31 },
  discover: { x: 415, y: 31 },
  board:    { x: 575, y: 31 },
  pro:      { x: 710, y: 31 },
  profile:  { x: 825, y: 31 },
};

// Tools page - "Try this" button on each card (2 rows x 4 cols)
const TOOL_CARDS = {
  interior:  { x: 310, y: 305 },   // Interior Design
  facade:    { x: 680, y: 305 },   // Exterior Design
  garden:    { x: 1060, y: 305 },  // Garden Design
  paint:     { x: 1430, y: 305 },  // Smart Wall Paint
  floor:     { x: 310, y: 520 },   // Floor Design
  layout:    { x: 680, y: 520 },   // Layout Makeover
  replace:   { x: 1060, y: 520 },  // Replace Furniture
  reference: { x: 1430, y: 520 },  // Reference Style
};

// ═══════════════════════════════════════════════════
// WIZARD FLOW
// ═══════════════════════════════════════════════════
async function wizardFlow(page, tool) {
  console.log(`\n  [WIZARD] ${tool}`);
  await go(page, "/tools");

  // Click on the tool card "Try this" button
  const card = TOOL_CARDS[tool];
  await clickAt(page, card.x, card.y, `tool_card_${tool}`);
  await page.waitForTimeout(3000);
  await shot(page, `wizard_${tool}_01_upload`);

  // Click "Try with an example" button (center of page)
  await clickAt(page, 730, 520, "try_example");
  await page.waitForTimeout(3000);
  await shot(page, `wizard_${tool}_02_example`);

  // Progress through steps by clicking "Next" button (bottom right)
  for (let step = 3; step <= 8; step++) {
    // Select first option (left side of content area)
    await clickAt(page, 350, 400, `select_option_${step}`);
    await page.waitForTimeout(800);

    // Click Next (bottom right)
    await clickAt(page, 1300, 855, `next_step_${step}`);
    await page.waitForTimeout(2000);
    await shot(page, `wizard_${tool}_${step}_step`);

    // Check if we hit Generate
    const pageText = await page.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      await shot(page, `wizard_${tool}_review`);
      break;
    }
  }
  await go(page, "/tools");
}

// ═══════════════════════════════════════════════════
// PAYWALL FLOW
// ═══════════════════════════════════════════════════
async function paywallFlow(page) {
  console.log("\n[PAYWALL FLOW]");
  await go(page, "/profile");

  // Click "Get Started Free" button
  await clickAt(page, 340, 475, "get_started");
  await page.waitForTimeout(2000);
  await shot(page, "paywall_01_triggered");

  // If auth opened, close it and try Pro page instead
  const bodyText = await page.textContent("body").catch(() => "");
  if (bodyText.includes("Sign In") || bodyText.includes("Continue with Google")) {
    await clickAt(page, 50, 30, "close_auth");
    await page.waitForTimeout(1000);
  }

  // Navigate to Pro page and click "Get Pro" to trigger paywall
  await go(page, "/pro");
  await clickAt(page, 700, 860, "get_pro");
  await page.waitForTimeout(2000);
  await shot(page, "paywall_01_sheet");

  // Click Continue through 5 steps (CTA button at bottom center of modal)
  for (let step = 2; step <= 5; step++) {
    await clickAt(page, 750, 770, `paywall_continue_${step}`);
    await page.waitForTimeout(1500);
    await shot(page, `paywall_0${step}_step${step}`);
  }

  // Close paywall
  await clickAt(page, 365, 107, "close_paywall");
  await page.waitForTimeout(1000);
}

// ═══════════════════════════════════════════════════
// SETTINGS FLOW
// ═══════════════════════════════════════════════════
async function settingsFlow(page) {
  console.log("\n[SETTINGS FLOW]");
  await go(page, "/profile");

  // Click gear icon (top right of profile, near the "150" diamond badge)
  await clickAt(page, 1300, 100, "gear_icon");
  await page.waitForTimeout(2000);
  await shot(page, "settings_01_main");

  // Scroll down to see all settings items
  await page.mouse.wheel(0, 300);
  await page.waitForTimeout(1000);
  await shot(page, "settings_02_scrolled");

  // Click Language row
  await clickAt(page, 700, 400, "language_row");
  await page.waitForTimeout(1500);
  await shot(page, "settings_03_language");
  // Close language picker
  await clickAt(page, 700, 700, "select_language");
  await page.waitForTimeout(1000);

  // Click Theme toggle
  await clickAt(page, 700, 470, "theme_row");
  await page.waitForTimeout(1000);
  await shot(page, "settings_04_theme");
  await clickAt(page, 700, 470, "theme_toggle_back");
  await page.waitForTimeout(800);

  // Click Send Feedback
  await clickAt(page, 700, 600, "feedback_row");
  await page.waitForTimeout(1500);
  await shot(page, "settings_05_feedback_dialog");
  // Cancel
  await clickAt(page, 600, 650, "cancel_feedback");
  await page.waitForTimeout(800);

  // Click Log Out
  await page.mouse.wheel(0, 400);
  await page.waitForTimeout(800);
  await clickAt(page, 700, 700, "logout_row");
  await page.waitForTimeout(1500);
  await shot(page, "settings_06_logout_dialog");
  await clickAt(page, 600, 650, "cancel_logout");
  await page.waitForTimeout(800);

  // Close settings
  await clickAt(page, 50, 30, "close_settings");
  await page.waitForTimeout(1000);
}

// ═══════════════════════════════════════════════════
// DISCOVER FLOW
// ═══════════════════════════════════════════════════
async function discoverFlow(page) {
  console.log("\n[DISCOVER FLOW]");
  await go(page, "/discover");
  await shot(page, "discover_01_main");

  // Click Architecture tab
  await clickAt(page, 745, 140, "tab_architecture");
  await page.waitForTimeout(1500);
  await shot(page, "discover_02_architecture");

  // Click Landscape tab
  await clickAt(page, 1120, 140, "tab_landscape");
  await page.waitForTimeout(1500);
  await shot(page, "discover_03_landscape");

  // Click back to Interior tab
  await clickAt(page, 385, 140, "tab_interior");
  await page.waitForTimeout(1500);
  await shot(page, "discover_04_interior");

  // Click first "See all" link (right side of Kitchen section header)
  await clickAt(page, 1280, 215, "see_all_1");
  await page.waitForTimeout(2000);
  await shot(page, "discover_05_detail");

  // Click first gallery card in detail view
  await clickAt(page, 350, 400, "gallery_card");
  await page.waitForTimeout(2000);
  await shot(page, "discover_06_preview_dialog");

  // Click "Create with this style" button in dialog
  await clickAt(page, 750, 650, "create_with_style");
  await page.waitForTimeout(2000);
  await shot(page, "discover_07_create_style");

  // Go back
  await go(page, "/discover");
}

// ═══════════════════════════════════════════════════
// AUTH FLOW
// ═══════════════════════════════════════════════════
async function authFlow(page) {
  console.log("\n[AUTH FLOW]");
  await go(page, "/profile");

  // Click "Get Started Free"
  await clickAt(page, 340, 475, "get_started");
  await page.waitForTimeout(3000);
  await shot(page, "auth_01_signin");

  // Click "Sign Up" link (bottom of form)
  await clickAt(page, 750, 650, "switch_to_signup");
  await page.waitForTimeout(2000);
  await shot(page, "auth_02_signup");

  // Close
  await clickAt(page, 50, 30, "close_auth");
  await page.waitForTimeout(1000);
}

// ═══════════════════════════════════════════════════
// BOARD FLOW
// ═══════════════════════════════════════════════════
async function boardFlow(page) {
  console.log("\n[BOARD FLOW]");
  await go(page, "/board");
  await shot(page, "board_01_main");

  // Click "Get Started Free" button in sidebar
  await clickAt(page, 1200, 505, "get_started_sidebar");
  await page.waitForTimeout(2000);
  await shot(page, "board_02_get_started");

  // Close auth if it opened
  const bodyText = await page.textContent("body").catch(() => "");
  if (bodyText.includes("Sign In") || bodyText.includes("Continue with Google")) {
    await clickAt(page, 50, 30, "close_auth");
    await page.waitForTimeout(1000);
  }

  // Go back to board
  await go(page, "/board");

  // Scroll down to see more gallery cards
  await page.mouse.wheel(0, 400);
  await page.waitForTimeout(1000);
  await shot(page, "board_03_scrolled");

  // Click on a gallery card (e.g., Living Room)
  await clickAt(page, 900, 260, "gallery_card_living");
  await page.waitForTimeout(2000);
  await shot(page, "board_04_card_detail");
}

// ═══════════════════════════════════════════════════
// PROFILE FLOW
// ═══════════════════════════════════════════════════
async function profileFlow(page) {
  console.log("\n[PROFILE FLOW]");
  await go(page, "/profile");
  await shot(page, "profile_01_main");

  // Click Help Center
  await clickAt(page, 700, 800, "help_center");
  await page.waitForTimeout(2000);
  await shot(page, "profile_02_help_center");
  await go(page, "/profile");

  // Click Contact Us
  await clickAt(page, 700, 870, "contact_us");
  await page.waitForTimeout(2000);
  await shot(page, "profile_03_contact_us");
  await go(page, "/profile");

  // Scroll down for more items
  await page.mouse.wheel(0, 400);
  await page.waitForTimeout(1000);
  await shot(page, "profile_04_scrolled");
}

// ═══════════════════════════════════════════════════
// MAIN
// ═══════════════════════════════════════════════════
(async () => {
  console.log(`\n=== HomeDecor AI - Coordinate-Based Screenshot Tool ===`);
  console.log(`URL: ${BASE_URL}`);
  console.log(`Output: ${SHOT_DIR}\n`);

  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const ctx = await browser.newContext({ viewport: { width: 1440, height: 900 }, deviceScaleFactor: 2 });
  const page = await ctx.newPage();
  page.on("console", m => { if (m.type() === "error") errors.push(m.text()); });
  page.on("pageerror", e => errors.push(e.message));

  // ─── PHASE 1: Main Pages ───
  console.log("═══ Phase 1: Main Pages ═══");
  for (const [name, path] of [["tools", "/tools"], ["discover", "/discover"], ["board", "/board"], ["pro", "/pro"], ["profile", "/profile"]]) {
    console.log(`\n[PAGE] ${path}`);
    await go(page, path);
    await shot(page, `page_${name}`);
  }

  // ─── PHASE 2: Discover Deep Dive ───
  console.log("\n═══ Phase 2: Discover ═══");
  await discoverFlow(page);

  // ─── PHASE 3: Board ───
  console.log("\n═══ Phase 3: Board ═══");
  await boardFlow(page);

  // ─── PHASE 4: Profile ───
  console.log("\n═══ Phase 4: Profile ═══");
  await profileFlow(page);

  // ─── PHASE 5: Wizard Flows ───
  console.log("\n═══ Phase 5: Wizards ═══");
  for (const tool of ["interior", "facade", "garden", "paint", "floor", "layout", "replace", "reference"]) {
    await wizardFlow(page, tool);
  }

  // ─── PHASE 6: Paywall ───
  console.log("\n═══ Phase 6: Paywall ═══");
  await paywallFlow(page);

  // ─── PHASE 7: Settings ───
  console.log("\n═══ Phase 7: Settings ═══");
  await settingsFlow(page);

  // ─── PHASE 8: Auth ───
  console.log("\n═══ Phase 8: Auth ═══");
  await authFlow(page);

  // ─── PHASE 9: Mobile ───
  console.log("\n═══ Phase 9: Mobile ═══");
  await ctx.close();
  const mCtx = await browser.newContext({ viewport: { width: 375, height: 812 }, deviceScaleFactor: 3, isMobile: true });
  const m = await mCtx.newPage();

  for (const [name, path] of [["tools", "/tools"], ["discover", "/discover"], ["board", "/board"], ["pro", "/pro"], ["profile", "/profile"]]) {
    console.log(`\n[MOBILE] ${path}`);
    await go(m, path);
    await shot(m, `mobile_${name}`);
  }

  // Mobile: bottom nav (5 items spread across 375px width at y≈780)
  await go(m, "/tools");
  await shot(m, "mobile_01_tools");
  await m.tap({ x: 75, y: 780 }); await m.waitForTimeout(1500); await shot(m, "mobile_02_discover");
  await m.tap({ x: 225, y: 780 }); await m.waitForTimeout(1500); await shot(m, "mobile_03_board");
  await m.tap({ x: 375, y: 780 }); await m.waitForTimeout(1500); await shot(m, "mobile_04_pro");
  await m.tap({ x: 525, y: 780 }); await m.waitForTimeout(1500); await shot(m, "mobile_05_profile");

  // Mobile settings gear
  await m.tap({ x: 350, y: 100 }); await m.waitForTimeout(2000); await shot(m, "mobile_06_settings");

  // ─── Error log ───
  if (errors.length > 0) {
    writeFileSync(join(SHOT_DIR, "console-errors.txt"), errors.join("\n"));
    console.log(`\n[ERRORS] ${errors.length} errors saved`);
  }

  await mCtx.close();
  await browser.close();
  console.log(`\n=== DONE! ${idx} screenshots saved to ${SHOT_DIR} ===\n`);
})();
