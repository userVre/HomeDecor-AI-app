# HomeDecor AI Web UI/UX Review - June 26, 2026

Live target reviewed: `http://localhost:8081/`

Evidence folders:

- `qa-artifacts/live-review-2026-06-26/`
- `qa-artifacts/live-review-continuation-2026-06-26/`
- `qa-artifacts/unblocked-review-2026-06-26/`
- `qa-artifacts/fresh-postfix-2026-06-26/`

Important limitation: the local app server is healthy on `http://localhost:8081/`, but the in-app Browser automation timed out repeatedly after the fresh Interior screenshot was captured. Treat this as a UI/UX audit with strong screenshot/source evidence, not as a fully completed final QA sign-off.

Fresh HTTP route check on June 26, 2026:

- `/` returns `200`.
- `/tools`, `/discover`, `/pro`, `/board`, `/profile`, `/create/interior`, and `/create/facade` return `404 Cannot GET ...` from the running dev server.
- `app/webpack.config.d/dev-server.js` contains `historyApiFallback: true`, but the active dev server is not serving deep links correctly.

## Evidence Coverage Matrix

| Area | Evidence | Status |
| --- | --- | --- |
| Tools desktop | `qa-artifacts/unblocked-review-2026-06-26/tools-desktop-after-fix.png` | Verified after breakpoint fix. Top nav and 3-column grid render. Visual polish still needed. |
| Tools mobile | `qa-artifacts/live-review-2026-06-26/tools-mobile.png` | Verified. Bottom nav overlaps/obscures the third tool card. |
| Discover desktop | `qa-artifacts/unblocked-review-2026-06-26/discover-desktop-after-fix.png` | Verified after breakpoint fix. Grid exists, but tabs and image cropping/repetition still look broken. |
| Discover mobile | `qa-artifacts/live-review-2026-06-26/discover-mobile.png` | Verified. Horizontal rows clip at the right edge and bottom nav covers lower content. |
| My Board desktop | `qa-artifacts/unblocked-review-2026-06-26/board-desktop-after-fix.png` | Verified. Reachable but sparse and placeholder-heavy. |
| Pro desktop | `qa-artifacts/unblocked-review-2026-06-26/pro-desktop-after-fix.png` | Verified as Upgrade screen, not auto-paywall, in captured evidence. Claims/pricing still need validation. |
| Direct route reloads | `Invoke-WebRequest` route check on June 26, 2026 | Confirmed failure. Only `/` returns 200; tested deep links return 404 from the dev server. |
| Profile desktop | `qa-artifacts/unblocked-review-2026-06-26/profile-desktop-after-fix.png` and source route order | Still needs clean live verification after deep-link 404 is fixed. Earlier screenshot showed Pro on Profile; source route order now checks `profile` before `pro`. |
| Interior wizard step 1 | `qa-artifacts/live-review-2026-06-26/interior-step1-1440.png`, `qa-artifacts/fresh-postfix-2026-06-26/interior-step1-postfix.png` | Verified step 1 only. Fresh post-fix screenshot exists, but complete step-through still needs live verification. |
| Exterior wizard step 1 | `qa-artifacts/live-review-2026-06-26/exterior-step1-1440.png` | Verified step 1 only. Older screenshot shows generic `Room Type`; source copy has now been patched but needs fresh visual verification. |
| Paywall mobile steps 2-4 | `qa-artifacts/live-review-2026-06-26/paywall-mobile-step2.png` through `paywall-mobile-step4.png` | Verified. Steps 2-4 render. |
| Paywall step 5 | `qa-artifacts/live-review-2026-06-26/paywall-mobile-step5.png` | Verified failure. The supposed step 5 screenshot is still `4 / 5`, so step 4 did not advance. |
| Full live Browser walkthrough | Browser bridge attempts on June 26, 2026 | Incomplete. Single-route screenshot attempts timed out, including a 120s `/profile` capture. |

## Current Verification Summary

The app runs on the local web server and the responsive breakpoint bug has been fixed in source:

- `app/src/wasmJsMain/kotlin/com/ismail/homedecorai/Platform.kt` now returns `window.innerWidth` instead of `window.innerWidth / 16`.
- Desktop screenshots after that change show top navigation and a 3-column Tools grid.

Several findings remain visible in screenshots or source:

- Deep-link reloads are currently broken at the dev-server level. `/tools`, `/discover`, `/pro`, `/board`, `/profile`, `/create/interior`, and `/create/facade` return 404 when requested directly.
- Profile deep-link behavior still needs hard-refresh verification after the 404 bug is fixed. Earlier browser evidence showed `/profile` displaying the Pro screen because `profile` matched the `pro` route prefix; the source route order has been corrected, but browser verification timed out afterward.
- `/pro` now has source wiring to show the Upgrade screen first and open paywall only from CTAs, but this still needs final browser verification after a clean reload.
- Tools and Discover are desktop-shaped now, but still need visual polish.
- My Board is reachable but sparse and placeholder-heavy on desktop.
- Wizard example mode exists in source, and context-specific Room/Exterior/Garden copy has now been fixed in `WebWizardScreen.kt`; first-step layout and live walkthrough still need browser verification.
- Wizard example mode is now exposed as a standalone visible CTA below the upload drop zone; live walkthrough still needs browser verification.
- Paywall has a 5-step source structure, and step 4 CTA now jumps explicitly to checkout in source; live verification is still required.
- Several subscription claims need product/legal validation before release.

## Mistakes Found

### Global / Navigation

1. Desktop breakpoint was broken before the source fix.
   The app treated desktop widths like mobile because web screen width was divided by 16. This is fixed in source, and post-fix screenshots show desktop navigation again.

2. Direct deep links currently fail with 404.
   The running dev server returns 404 for `/tools`, `/discover`, `/pro`, `/board`, `/profile`, `/create/interior`, and `/create/facade`. Users who refresh a screen or open a shared link will hit `Cannot GET ...` instead of the app.

3. Route matching had a high-risk `/profile` vs `/pro` bug.
   Earlier evidence showed Profile rendering the Pro screen. Source now checks `profile` before `pro`, but a clean Browser hard-refresh verification is still required.

4. Paywall entry behavior was confusing.
   `/pro` should show an Upgrade/Pricing page first. The source now avoids auto-opening paywall, but it still needs a clean reload verification.

5. Some desktop CTA wiring was no-op before the source patch.
   Upgrade/Profile/Board paywall CTAs were wired to empty lambdas in one desktop path. Source wiring has been corrected, but route-level verification remains open.

6. The app needs more stable accessibility/test hooks.
   The Compose web output is difficult to inspect through DOM snapshots, which makes QA rely too much on screenshots and coordinates.

### Tools Page

1. Tool cards are too dark.
   Heavy black overlays make real room/exterior/garden photos look like tinted blocks instead of inspectable examples.

2. Credit balance is duplicated.
   Desktop shows credit UI in the top navigation and again in the Tools header area.

3. Desktop grid rhythm is unfinished.
   Eight tools in a 3-column layout leaves an awkward final row unless it is intentionally balanced or redesigned.

4. Card typography/CTA scale feels mobile-sized on desktop.
   The visual hierarchy does not fully use the larger desktop card area.

5. Mobile bottom navigation overlap was visible.
   `tools-mobile.png` shows the fixed bottom nav covering lower card content.

### Discover Page

1. Desktop tabs look broken.
   Interior / Architecture / Landscape are spaced too far apart, and the selected underline reads like a full-width rule rather than a tab indicator.

2. Image cropping/repetition looks accidental.
   Several cards show partial duplicate-looking image fragments or overly tight crops.

3. `See all` placement is weak.
   It sits too far from the section title and does not feel clearly associated with the row/grid it controls.

4. Mobile carousel/content clipping is visible.
   Older mobile evidence shows right-edge clipping and bottom nav overlap. Source padding was increased, but fresh Browser verification failed.

### My Board

1. Signed-out desktop layout is sparse.
   The hero panel has a large empty center and tiny preview stack pushed far right.

2. Preview cards do not communicate value.
   Generated/Favorites/Projects cards look like placeholders or locked skeletons instead of saved designs.

3. Tabs feel disconnected.
   Generated / Favorites / Projects span too widely relative to the content below.

### Profile

1. Profile direct URL requires final hard-refresh verification.
   Source is likely fixed, but the last reliable Browser screenshot before the source fix showed the wrong screen.

2. Signed-out and upgrade actions need UX verification.
   Profile has paywall/sign-in entry points in source, but the full desktop/mobile interaction pass is still missing.

### Interior / Exterior / Garden Wizard

1. Step 1 upload area is oversized on desktop.
   The first-step layout has too much empty vertical space and pushes action discovery down.

2. Example mode was not visible in older screenshots.
   Source now renders a standalone `Try with an example` button below the upload drop zone, but complete live verification is still missing.

3. Tool-specific labels were generic before patching.
   Exterior showed `Room Type` in older evidence. Source now uses `Exterior Type`; Garden now uses `Outdoor Area`; Floor/Replace/Layout also have contextual labels.

4. The full wizard path is not proven yet.
   Interior, Exterior, and Garden still need step-by-step screenshots through Review using example mode.

5. Validation copy must match the selected tool.
   The source was improved, but live QA must prove the right message appears on each tool path.

### Paywall / Upgrade

1. Step 4 did not advance to Step 5 in captured evidence.
   The supposed Step 5 screenshot was still labeled `4 / 5`. Source now has an explicit `goCheckout()` path, but Browser verification is still required.

2. The 5-step flow has too many claims that may be unsupported.
   Unlimited generations, HD/4K export, no watermark, priority processing, free trial, `$0.00`, family/team plan, trusted users, and pricing all need product/legal validation.

3. Desktop paywall may feel like a phone sheet.
   The paywall needs verification and likely a desktop-centered modal treatment.

4. Back/close behavior needs full QA.
   Steps 2-4 are captured, but dismissal and back navigation still need a complete pass after the step-5 fix.

## Improvement Ideas

1. Make Tools the primary task launcher.
   Use a clean 4x2 or responsive 3/2/1 grid, lighter photo overlays, clearer short descriptions, and one visible action per card.

2. Turn Discover into inspiration browsing, not a broken feed.
   Use compact tabs, stable card sizes, good crops, fewer adjacent duplicate images, and a stronger relationship between section title and `See all`.

3. Make wizard step 1 faster.
   Put upload, example mode, and next action in one compact first viewport. Users should understand they can try the product without preparing a file.

4. Give each tool its own language.
   Interior should say room, Exterior should say exterior/building type, Garden should say outdoor area, Floor should say floor area/material, Replace should say furniture type, and Layout should say layout type.

5. Make signed-out Board/Profile states sell the workflow.
   Show meaningful locked previews and clear account benefits instead of empty placeholder space.

6. Simplify the paywall.
   Keep only true claims, make pricing exact, and shorten the flow if analytics do not prove all five steps help conversion.

7. Add QA semantics.
   Add content descriptions/test tags for screens, cards, tabs, wizard controls, paywall steps, and bottom/top navigation so future reviews do not depend on screenshot coordinates.

## Confirmed / High Confidence Issues

1. Tools visual treatment is too dark and duplicated.
   Desktop Tools now uses a 3-column grid, but real photos are buried under heavy color overlays, making cards look like tinted placeholders. A duplicate credit chip appears in both the top nav and page header area.
   Source support: `SharedToolsScreen.kt` has both `ToolsHeader` credit UI and card overlays up to `Color.Black.copy(alpha = 0.68f)`.

2. Discover desktop grid has image repetition/cropping and tab spacing problems.
   The first desktop row repeats/crops similar kitchen images, including partial duplicate fragments. The Interior tab underline stretches like a layout rule, while Architecture/Landscape are far apart and visually disconnected.

3. My Board desktop composition is sparse.
   The hero panel has a very large empty center, tiny preview stack at the far right, and generated cards that look like locked placeholders rather than saved design previews.

4. Profile route bug was fixed in source but needs final verification.
   Source now checks `profile` before `pro` in `App.kt`, but the last captured Profile screenshot still showed the Pro page because it was taken before final route verification succeeded.

5. Wizard context copy was fixed in source but still needs visual verification.
   `WebWizardScreen.kt` now uses tool-aware labels for Exterior, Garden, Floor, Replace Furniture, and Layout. The in-app browser timed out before the Interior/Exterior walkthrough could be captured, so screenshots of those steps are still missing.

6. Wizard first step source has been improved but needs fresh screenshots.
   Interior and Exterior step 1 screenshots show a huge upload area and no visible example path. `Try with an example` is now a standalone button below the upload drop zone in source, but needs a fresh visual pass.

7. Mobile bottom navigation overlap is partially source-fixed and needs fresh screenshots.
   Tools already had large bottom padding. Discover now uses larger `navBarBottomPadding(120.dp)` for the main and detail lists. Verify Tools, Discover, Board, Profile, and Pro on mobile after a clean reload.

8. Paywall step 4 source has been hardened and needs fresh verification.
   `paywall-mobile-step5.png` is still labeled `4 / 5` and still shows plan selection, not checkout. `SharedPaywallSheet.kt` now sends step 4 CTA through an explicit `goCheckout()` path that selects the plan and sets `currentStep = TOTAL_STEPS`.

9. Paywall claims need validation.
   Copy includes unlimited AI generations, HD/4K export, no watermark, priority processing, 7-day free trial, `$0.00`, team/family plan, and pricing. Keep only what the real web product supports.

10. Browser accessibility/testability is weak.
   Compose canvas output makes automated DOM review difficult. More meaningful semantics are needed for navigation, screen headings, cards, wizard controls, and paywall controls.

## Parallel Fix Prompts

### Prompt 1 - Route Loading, Profile Deep Link, And Paywall Entry

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Fix and verify route loading for the Compose web app.

Context:
- Fresh HTTP checks show only http://localhost:8081/ returns 200.
- Direct requests to /tools, /discover, /pro, /board, /profile, /create/interior, and /create/facade return 404 "Cannot GET ...".
- app/webpack.config.d/dev-server.js contains historyApiFallback: true, but the active dev server is not honoring it.
- The source currently checks profile before pro in app/src/commonMain/kotlin/com/ismail/homedecorai/App.kt.
- paywallVisible is initialized false, and Upgrade/Profile CTAs are wired to onOpenPaywall.
- Earlier browser evidence showed /profile displaying the Pro page because "profile" matched the "pro" prefix.

First fix the dev-server/deployment fallback:
- Ensure every non-asset route returns index.html in development.
- Ensure the production/static host config also falls back to index.html for SPA routes.
- Confirm the Kotlin/webpack task actually consumes app/webpack.config.d/dev-server.js or move the fallback config to the location the task uses.

Verify with a clean dev server, fresh tab, hard refresh, and browser back/forward:
- http://localhost:8081/
- http://localhost:8081/tools
- http://localhost:8081/discover
- http://localhost:8081/pro
- http://localhost:8081/board
- http://localhost:8081/profile
- http://localhost:8081/create/interior
- http://localhost:8081/create/facade

Requirements:
- HTTP status for every route above must be 200 on direct request.
- Every direct URL opens the matching screen.
- /profile never displays the Pro screen.
- /pro shows the Upgrade/Pricing screen first, not the paywall modal.
- Paywall opens only after clicking a visible plan/upgrade CTA.
- Browser back/forward and refresh preserve the correct screen.
- No permanent loader and no normal-route "Something went wrong" fallback.

Deliver screenshots for each route at 1440x900 and one mobile smoke test at 390x844.
```

### Prompt 2 - Tools Page Visual Polish

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Polish the Tools page after the desktop breakpoint fix.

Current verified state:
- Desktop top nav is visible.
- Tools uses a 3-column grid at 1440x900.

Remaining issues from qa-artifacts/unblocked-review-2026-06-26/tools-desktop-after-fix.png:
- Tool photos are too dark/heavily tinted and read like color blocks.
- The page duplicates the credit balance: one in the nav and another floating near the Tools header.
- The grid leaves an awkward empty third slot on the last row.
- Card text and CTA treatment feel mobile-sized inside large desktop cards.
- Mobile evidence in qa-artifacts/live-review-2026-06-26/tools-mobile.png shows the bottom nav covering the Garden card.

Requirements:
- Keep all real tool image assets visible: Interior, Exterior, Garden, Wall Paint, Floor, Layout, Replace Furniture, Reference Style.
- Reduce overlays enough that the photo subject is inspectable while preserving text contrast.
- Remove duplicate credit chip from the page header when the top nav already shows credits.
- Make the final grid row intentional: balanced 4x2, responsive 3/2/1 columns, or a designed featured card.
- Tune desktop card height, text position, and CTA sizing for a professional web tool grid.
- Preserve mobile stacked layout and add enough bottom content padding so the fixed bottom nav never overlaps cards.

Verify at 1440x900, 1280x720, and 390x844.
```

### Prompt 3 - Discover Page Grid, Tabs, And Photo Quality

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Fix Discover page visual layout and photo presentation.

Current verified state:
- Desktop top nav is visible.
- Discover no longer uses the old mobile bottom nav at 1440x900.

Issues from qa-artifacts/unblocked-review-2026-06-26/discover-desktop-after-fix.png:
- Interior / Architecture / Landscape tabs are spaced like a broken full-width rule instead of a clear segmented/tab control.
- The selected Interior underline stretches across a huge area.
- Kitchen cards repeat/crop similar images, including partial duplicate image fragments.
- Some cards show only slices of an image, which feels like an asset/crop bug.
- "See all" is too far from the section title and easy to miss.
- Mobile evidence in qa-artifacts/live-review-2026-06-26/discover-mobile.png shows right-edge carousel clipping and bottom-nav overlap.
- Source note: the bottom padding was raised to `navBarBottomPadding(120.dp)` after that screenshot; re-verify before doing more bottom-inset work.

Requirements:
- Create a clear responsive tab control with balanced spacing on desktop and mobile.
- Use a desktop grid with stable card dimensions and intentional gaps.
- Fix image cropping so each card shows a complete, useful room/exterior/garden view.
- Avoid adjacent duplicate-looking images in the same row.
- Keep real images, not abstract placeholders or heavy overlays.
- Ensure mobile horizontal rows do not clip at the right edge and bottom nav does not cover content.

Verify Interior, Architecture, and Landscape tabs at 1440x900 and 390x844.
```

### Prompt 4 - Wizard Flow, Example Mode, And Step Verification

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Verify and finish the creation wizard for Interior, Exterior, and Garden.

Source context:
- app/src/commonMain/kotlin/com/ismail/homedecorai/ui/tools/WebWizardScreen.kt has onTryExample wired and Strings.wizardTryExample exists.
- Context-specific step labels and validation copy were patched in WebWizardScreen.kt on June 26, 2026.
- The patch compiled with :app:compileKotlinWasmJs.
- Live browser walkthrough is still missing because the Browser bridge timed out during route capture.

Requirements:
- Confirm "Try with an example" is visibly available on step 1 for Interior, Exterior, Garden, Floor, Wall Paint, Layout, Replace Furniture, and Reference Style where relevant. Existing screenshots `interior-step1-1440.png` and `exterior-step1-1440.png` do not show it, but source now renders a standalone button below the upload drop zone.
- Confirm Interior labels: Upload Photo, Room Type, Style, Review.
- Confirm Exterior labels: Upload Photo, Exterior Type, Style, Review.
- Confirm Garden labels: Upload Photo, Outdoor Area, Style, Review.
- Confirm validation messages match the selected tool.
- Example mode must enable Next and let QA reach Review without uploading a user file.
- Desktop wizard spacing should not feel vertically empty or detached.
- Mobile wizard controls must stay above the bottom safe area.

Acceptance:
- Complete Interior through Review using example mode.
- Complete Exterior through Review using example mode.
- Complete Garden through Review using example mode.
- Save screenshots of each step for Interior and Exterior.
```

### Prompt 5 - Paywall 5-Step Flow And Claim Audit

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Verify and harden the 5-step paywall flow.

Source context:
- app/src/commonMain/kotlin/com/ismail/homedecorai/ui/paywall/SharedPaywallSheet.kt defines TOTAL_STEPS = 5.
- Step 4 plan selection uses PaywallStep4Plans.
- Step 5 checkout exists as PaywallStep5Checkout.
- Bottom CTA calls goNext when currentStep < 5.
- Captured evidence: qa-artifacts/live-review-2026-06-26/paywall-mobile-step5.png is still labeled 4 / 5 and still shows the step 4 plan selection screen.
- Source note: step 4 now uses an explicit goCheckout path that sets currentStep to TOTAL_STEPS. Re-test before making deeper paywall state changes.

Required flow:
1/5 Create unlimited room makeovers
2/5 Reminder selection
3/5 Free vs Premium comparison
4/5 Plan selection after 7-day free trial
5/5 Checkout/payment interface

Requirements:
- Starting from /pro, paywall opens only after clicking a plan/upgrade CTA.
- Step 4 CTA reliably advances to checkout step 5.
- Step 5 clearly shows checkout/payment summary and is labeled 5/5.
- Back button moves one step back.
- Close dismisses paywall.
- Desktop paywall should be a polished centered modal or a web-appropriate sheet, not a stretched phone modal.

Claim audit:
- Remove or soften claims unless they are true in the web product: unlimited generations, HD/4K export, no watermark, priority processing, trusted users, free trial, $0.00, family/team plan.
- Pricing and plan names must match real product configuration.

Verify at 1440x900 and 390x844.
```

### Prompt 6 - My Board Empty/Signed-Out State

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Polish the My Board desktop and mobile signed-out state.

Issue from qa-artifacts/unblocked-review-2026-06-26/board-desktop-after-fix.png:
- Desktop hero panel has a huge empty center.
- Preview cards are tiny and pushed to the far right.
- Generated cards look like low-detail placeholders instead of useful locked previews.
- Tabs Generated / Favorites / Projects span too widely and feel disconnected.

Requirements:
- Redesign the signed-out board state as a balanced desktop layout.
- Keep the primary Sign In / Register action clear.
- Use meaningful preview imagery or designed locked cards that communicate saved designs.
- Make Generated / Favorites / Projects tabs compact and clearly related to the content below.
- Ensure mobile state remains readable and bottom nav does not overlap content.

Verify at 1440x900 and 390x844.
```

### Prompt 7 - Accessibility And Automated QA Semantics

```text
Work only in C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App).

Improve accessibility and testability for the Compose web app.

Observed issue:
- The visually rendered app is hard to inspect through browser DOM/accessibility snapshots.
- Automated route and UI review depends too much on screenshots/coordinates.

Requirements:
- Add meaningful semantics/content descriptions for main navigation items, screen headings, tool cards, Discover tabs/cards, Board tabs/cards, wizard steps, upload/example controls, and paywall controls.
- Ensure the skip link targets a real main content target.
- Expose stable route/screen state for QA where practical.
- Keep visible focus states for keyboard navigation.
- Add or update browser/Compose tests for route loads and key controls where the project supports it.

Acceptance:
- Browser/accessibility snapshots expose meaningful labels.
- Keyboard users can navigate main routes, tool cards, wizard controls, and paywall controls.
- Automated tests can identify screens and key CTAs without relying on coordinates.
```

## Recommended Parallel Order

1. Run Prompt 1 first or alone because it verifies route correctness after the source fixes.
2. Prompts 2, 3, and 6 can run in parallel because they touch separate screens.
3. Prompt 4 can run in parallel with visual polish if no one else is editing `WebWizardScreen.kt`.
4. Prompt 5 can run in parallel unless another chat is editing paywall strings/components.
5. Prompt 7 should run after or alongside the screen work, but avoid conflicting edits in the same composables.
