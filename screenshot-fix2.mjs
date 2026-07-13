import { chromium } from "playwright";
import { readdirSync } from "fs";
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

(async () => {
  console.log(`\n=== Fix paint & reference landings (from #${idx + 1}) ===`);
  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const ctx = await browser.newContext({ viewport: { width: 1440, height: 900 }, deviceScaleFactor: 2 });
  const page = await ctx.newPage();

  await go(page, "/tools");

  // First, take a debug screenshot to see the full layout with scroll
  await page.screenshot({ path: join(SHOT_DIR, "debug_tools_full.png"), fullPage: true });
  console.log("  [DEBUG] Full tools page screenshot saved");

  // Try clicking Smart Wall Paint (2nd row, left column)
  // From the screenshot, it appears at roughly x=310, y=520
  console.log("\n--- Attempting Paint (x=310, y=520) ---");
  await clickAt(page, 310, 520, "attempt_paint");
  await page.waitForTimeout(3000);
  const url1 = page.url();
  console.log(`  URL after click: ${url1}`);
  if (!url1.includes("/tools")) {
    await shot(page, "fix_desktop_wizard_paint_01_landing");
    // Continue with "See it in action"
    await clickAt(page, 730, 520, "see_it_in_action");
    await page.waitForTimeout(3000);
    await shot(page, "fix_desktop_wizard_paint_02_example");
    for (let step = 3; step <= 10; step++) {
      await clickAt(page, 1300, 855, `next_step${step}`);
      await page.waitForTimeout(2500);
      await shot(page, `fix_desktop_wizard_paint_step${step}`);
      const pageText = await page.textContent("body").catch(() => "");
      if (pageText.includes("Generate") && !pageText.includes("Next")) {
        await shot(page, "fix_desktop_wizard_paint_generate");
        break;
      }
    }
  } else {
    console.log("  Still on tools page, paint click failed");
  }

  await go(page, "/tools");

  // Try Reference Style (3rd row, middle column)
  // Scroll down to see 3rd row, then click
  console.log("\n--- Attempting Reference (scrolling to 3rd row) ---");
  await page.evaluate(() => window.scrollTo(0, 400));
  await page.waitForTimeout(500);
  // After scrolling 400px, the 3rd row should be more visible
  // Reference is in the middle column of 3rd row
  // Let me try x=680 (middle), y that's visible after scroll
  // The 3rd row cards start at about y=700 from top, minus 400 scroll = y=300 viewport
  // But actually the "Reference Style" card in the 3rd row middle...
  // Let me just try clicking at different positions
  
  // From the full-page screenshot, Reference Style appears to be at about x=1050, y=1100 (full page coords)
  // In viewport after scrolling 400px: x=1050, y=1100-400=700
  await clickAt(page, 1050, 700, "attempt_reference_scroll400");
  await page.waitForTimeout(3000);
  let url2 = page.url();
  console.log(`  URL after scroll click: ${url2}`);
  
  if (url2.includes("/tools")) {
    // Try different position
    await go(page, "/tools");
    await page.evaluate(() => window.scrollTo(0, 300));
    await page.waitForTimeout(500);
    await clickAt(page, 1060, 700, "attempt_reference_v2");
    await page.waitForTimeout(3000);
    url2 = page.url();
    console.log(`  URL after v2: ${url2}`);
  }
  
  if (url2.includes("/tools")) {
    // Try yet another position - maybe the card is wider than I think
    await go(page, "/tools");
    await page.evaluate(() => window.scrollTo(0, 500));
    await page.waitForTimeout(500);
    await clickAt(page, 1200, 650, "attempt_reference_v3");
    await page.waitForTimeout(3000);
    url2 = page.url();
    console.log(`  URL after v3: ${url2}`);
  }

  if (!url2.includes("/tools")) {
    await shot(page, "fix_desktop_wizard_reference_01_landing");
    await clickAt(page, 730, 520, "see_it_in_action");
    await page.waitForTimeout(3000);
    await shot(page, "fix_desktop_wizard_reference_02_example");
    for (let step = 3; step <= 10; step++) {
      await clickAt(page, 1300, 855, `next_step${step}`);
      await page.waitForTimeout(2500);
      await shot(page, `fix_desktop_wizard_reference_step${step}`);
      const pageText = await page.textContent("body").catch(() => "");
      if (pageText.includes("Generate") && !pageText.includes("Next")) {
        await shot(page, "fix_desktop_wizard_reference_generate");
        break;
      }
    }
  } else {
    console.log("  Could not navigate to reference wizard");
  }

  await ctx.close();
  await browser.close();
  console.log(`\n=== DONE! ${idx} total screenshots ===\n`);
})();
