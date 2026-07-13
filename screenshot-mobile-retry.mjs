import { chromium } from "playwright";
import { mkdirSync, existsSync } from "fs";
import { join } from "path";

const BASE_URL = process.env.APP_URL || "http://localhost:8081";
const SHOT_DIR = join(process.cwd(), "screenshots");

// Find the highest existing index
const fs = await import("fs");
const existing = fs.readdirSync(SHOT_DIR).filter(f => f.endsWith(".png")).map(f => parseInt(f.split("_")[0]));
let idx = Math.max(...existing);

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

async function tapAt(page, x, y, label) {
  await page.touchscreen.tap(x, y);
  await page.waitForTimeout(1500);
  console.log(`  [TAP] ${label || `(${x},${y})`}`);
}

// Tool card names and their "Try this" button text for mobile
const TOOLS = [
  { name: "interior", buttonText: "Redesign room" },
  { name: "facade", buttonText: "Restyle exterior" },
  { name: "garden", buttonText: "Plan garden" },
  { name: "paint", buttonText: "Preview colors" },
  { name: "floor", buttonText: "Try flooring" },
  { name: "layout", buttonText: "Optimize layout" },
  { name: "replace", buttonText: "Redesign" },
  { name: "reference", buttonText: "Try style" },
];

// Bad mobile wizard screenshots to re-do (showing tools page = 1815KB)
const FAILED = ["facade", "garden", "paint", "layout", "replace", "reference"];

async function mobileWizardFlow(page, tool) {
  console.log(`\n  [MOBILE_WIZARD] ${tool}`);
  await go(page, "/tools");

  // On mobile, cards stack vertically. Scroll to find the right card, then tap its CTA button.
  // Use locator to find the button by text
  const toolInfo = TOOLS.find(t => t.name === tool);
  
  // Try tapping by the CTA button text
  try {
    const btn = page.getByRole("button", { name: toolInfo.buttonText });
    await btn.scrollIntoViewIfNeeded();
    await page.waitForTimeout(500);
    await btn.tap();
    console.log(`  [TAP] CTA button "${toolInfo.buttonText}"`);
  } catch (e) {
    console.log(`  [WARN] Could not find button "${toolInfo.buttonText}", trying text match...`);
    // Fallback: tap by text content
    try {
      await page.getByText(toolInfo.buttonText).first().tap();
    } catch (e2) {
      console.log(`  [ERROR] Could not tap tool card for ${tool}`);
      return;
    }
  }
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${tool}_01_landing`);

  // Click "See it in action" / "Try with an example"
  try {
    const exampleBtn = page.getByText("See it in action").first();
    await exampleBtn.scrollIntoViewIfNeeded();
    await page.waitForTimeout(500);
    await exampleBtn.tap();
    console.log(`  [TAP] See it in action`);
  } catch (e) {
    try {
      await page.getByText("Try with an example").first().tap();
      console.log(`  [TAP] Try with an example`);
    } catch (e2) {
      console.log(`  [WARN] Could not find example button`);
    }
  }
  await page.waitForTimeout(3000);
  await shot(page, `mobile_wizard_${tool}_02_example`);

  // Click Next through wizard steps
  for (let step = 3; step <= 10; step++) {
    try {
      const nextBtn = page.getByRole("button", { name: "Next" });
      await nextBtn.scrollIntoViewIfNeeded();
      await page.waitForTimeout(300);
      await nextBtn.tap();
      console.log(`  [TAP] Next (step ${step})`);
    } catch (e) {
      // Try tapping by arrow character
      try {
        await page.getByText("→ Next").first().tap();
      } catch (e2) {
        console.log(`  [WARN] Could not find Next button at step ${step}`);
      }
    }
    await page.waitForTimeout(2500);
    await shot(page, `mobile_wizard_${tool}_step${step}`);

    const pageText = await page.textContent("body").catch(() => "");
    if (pageText.includes("Generate") && !pageText.includes("Next")) {
      console.log(`  [DONE] ${tool} reached Generate at step ${step}`);
      await shot(page, `mobile_wizard_${tool}_generate`);
      break;
    }
  }
  await go(page, "/tools");
}

(async () => {
  console.log(`\n=== Mobile Wizard Retry (from #${idx + 1}) ===`);

  const browser = await chromium.launch({ headless: false, args: ["--no-sandbox"] });
  const mCtx = await browser.newContext({
    viewport: { width: 375, height: 812 },
    deviceScaleFactor: 3,
    isMobile: true,
    hasTouch: true,
  });
  const m = await mCtx.newPage();

  for (const tool of FAILED) {
    await mobileWizardFlow(m, tool);
  }

  await mCtx.close();
  await browser.close();
  console.log(`\n=== DONE! ${idx} total screenshots ===\n`);
})();
