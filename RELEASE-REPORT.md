# HomeDecor AI - Pre-Publication Audit Report

**Date:** 2026-07-18
**Auditor:** Release Engineer (AI)
**App Version:** 1.1.0-native (versionCode 37)
**Target:** Web (WasmJS) + Android

---

## Build Status Summary

| Component | Status | Details |
|-----------|--------|---------|
| **Web production build (WasmJS)** | PASSED | `wasmJsBrowserDistribution` BUILD SUCCESSFUL (9m 47s) |
| **Kotlin WasmJS compilation** | PASSED | ~60 warnings, 0 errors |
| **Android compilation** | FAILED | 20+ errors across 8 files (pre-existing) |
| **Convex TypeScript type-check** | FAILED | 57 errors (40 missing @types/node + 17 real) |
| **Unit tests (commonTest)** | BLOCKED | Cannot run - Android compilation fails first |
| **Linting** | N/A | No ESLint/Ktlint/Detekt configured |

---

## 1. Fixed Issues (This Session)

### F-1: Missing image assets in composeResources/drawable/ (CRITICAL BUILD BLOCKER)
- **File:** `NetworkImage.wasmJs.kt:117-145`
- **Fix:** Copied 29 `.webp` image files from `wasmJsMain/resources/images/` to `commonMain/composeResources/drawable/`
- **Impact:** Without this fix, the web production build fails with 38 `Unresolved reference` errors for materials, floors, styles, garden, and exterior images
- **Files added:** `assets_media_materials_*.webp` (10), `assets_media_floor_*.webp` (4), `assets_media_styles_example_*.webp` (13), `assets_media_garden_landscapedpath.webp` (1), `assets_media_exterior_modernhouse.webp` (1)

---

## 2. Remaining Issues

### CRITICAL (Block publish)

| # | Issue | File | Details |
|---|-------|------|---------|
| C-1 | **Android build completely broken** | `HomeDecorViewModel.kt`, `Models.kt`, `MyBoardScreen.kt`, `SharedComponents.kt`, `DesignViewerSheet.kt`, `ProfileScreen.kt`, `SettingsScreen.kt`, `WizardSteps.kt` | 20+ compilation errors: `imageRes` parameter removed from `BoardItem` but Android code still references it; missing `IconButton` import; missing `ProfileScreenState`, `SettingsScreenState`, `SettingsLanguage` types |
| C-2 | **Client-controlled subscription spoofing** | `convex/users.ts:1138-1160` | `setViewerPlanFromRevenueCat` is a `mutationGeneric` that accepts client-supplied `plan`/`subscriptionType` with NO server-side RevenueCat verification. Any user can grant themselves Pro access. |
| C-3 | **Client-reported diamond purchase spoofing** | `convex/users.ts:1162-1239` | `fulfillDiamondPurchase` trusts client-supplied `transactionId`/`packId`/`amount` with no RevenueCat webhook verification on the mutation path. Users can self-report purchases. |
| C-4 | **`.env.local` tracked in git** | `.env.local` | Contains secrets. While `.gitignore` lists it, `git ls-files` confirms it's committed. Risk of secret leakage on branch merges or `.gitignore` changes. |

### HIGH (Fix before launch)

| # | Issue | File | Details |
|---|-------|------|---------|
| H-1 | **Client never sends idempotencyKey** | `WebWizardScreen.kt:1067-1078` | Server supports idempotency dedup (`convex/generations.ts:736-763`) but client never sends one. Double-clicks/retries create duplicate generation records. |
| H-2 | **No rate limiting on AI endpoints** | `convex/generations.ts`, `convex/ai.ts:753-877`, `convex/aiNode.ts:1045-1134` | `startGeneration`, `suggestDesignOptions`, `detectEditMask`, and `renderOnboardingDemo` are all public mutations with no per-user rate limiting. Paid users can burn unlimited Azure credits. |
| H-3 | **CORS `anyHost()` on Ktor server** | `server/.../Application.kt:48-56` | Allows requests from ANY origin. Combined with `allowHeader("Authorization")` and `allowHeader("X-User-Id")`, enables CSRF and credential theft. |
| H-4 | **No Content Security Policy headers** | `vercel.json`, `Application.kt` | No CSP configured anywhere. Vulnerable to XSS. `index.html` includes inline `<script>` blocks. |
| H-5 | **Wizard bottom bar back bypasses discard confirmation** | `WebWizardScreen.kt:1559-1568` | Bottom bar "Back" calls `navigateBack()` directly without `hasUserData()` check. Header back (line 1283-1292) correctly shows discard dialog. User loses progress without warning. |
| H-6 | **Browser back exits entire wizard** | `App.kt:150-176, 496-511` | Wizard step changes never call `pushHistoryState`. Browser back at step 3 exits the whole wizard instead of going to step 2. `popstate` listener can't restore wizard step state. |
| H-7 | **`hasUserData()` incomplete** | `WebWizardScreen.kt:1165-1168` | Only checks `photo`, `selectedRoom`, `selectedStyle`, `selectedMaterial`, `selectedGoals`. Misses `selectedPalette`, `selectedTransferStrength`, `replacementPrompt`, `selectedPaintColor`, `selectedFloorStyle`, `selectedFurnitureType`, `referencePhoto`, `customNotes`, etc. |

### MEDIUM (Fix this sprint)

| # | Issue | File | Details |
|---|-------|------|---------|
| M-1 | **Validation hint says "room type" for garden/facade** | `Strings.kt:606`, `WizardScaffold.kt:675`, `WebWizardScreen.kt:4662` | Uses generic `wizardHintRoomType` instead of per-tool hints defined at `WebWizardScreen.kt:809-814`. |
| M-2 | **Test mismatch: "Outdoor Style" vs "Garden Style"** | `WizardScaffoldTest.kt:33` vs `WizardScaffold.kt:642` | Test expects `"Outdoor Style"` but code returns `"Garden Style"`. Will fail. |
| M-3 | **No client-side balance check before generation** | `WebWizardScreen.kt:4524` | Generate button only checks `!isGenerating && !generationComplete`. Zero-diamond users can still click Generate (wastes network round-trip). |
| M-4 | **Missing `WindowInsets.ime` on wizard footer** | `WizardScaffold.kt`, `WebWizardScreen.kt` | Footer buttons can be hidden behind mobile virtual keyboard. |
| M-5 | **Low contrast validation hint text** | `WizardScaffold.kt:574`, `WebWizardScreen.kt:4647` | `onSurfaceVariant.copy(alpha = 0.7f)` yields ~3.2:1 contrast ratio. WCAG AA requires 4.5:1. |
| M-6 | **Prompt injection via `customPrompt`** | `convex/generations.ts:807-818` | `customPrompt` is trimmed but not length-limited or sanitized. Attacker can inject instructions. |
| M-7 | **Guest user authentication spoofable** | `convex/viewer.ts:157-197` | Guest users authenticate via client-provided `anonymousId` string. Server can't verify legitimacy. |
| M-8 | **Webhook auth bypass** | `convex/http.ts:60-63, 126-129` | If `REVENUECAT_WEBHOOK_AUTH` env var is not set, auth check is skipped entirely (fail-open). |
| M-9 | **`getGenerationByOrderId` leaks data** | `convex/generations.ts:1141-1158` | Public query with no auth. Anyone who guesses an orderId gets full generation data. |
| M-10 | **Two-phase reserve/finalize stuck slots** | `convex/generations.ts:295-498` | If Convex scheduler crashes between reserve and finalize, generation stays "processing" forever, consuming one credit slot permanently. |

---

## 3. Failed Tests

| Test | Status | Reason |
|------|--------|--------|
| `WizardScaffoldTest.kt:33` | WILL FAIL | Expects `"Outdoor Style"` but code returns `"Garden Style"` |
| All `commonTest` tests | CANNOT RUN | Android compilation fails, no JVM test target configured |
| WasmJS tests | CANNOT RUN | `wasmJsTest/` directory is empty |
| Convex TypeScript | 57 ERRORS | 40 missing `@types/node` + 17 real type errors (see below) |

### Convex TypeScript Real Errors (beyond @types/node)

| File | Line | Error |
|------|------|-------|
| `aiNode.ts` | 500 | `useDeploymentBasedUrls` does not exist in `AzureOpenAIProviderSettings` |
| `aiNode.ts` | 1116 | `mediaType` does not exist on `GeneratedFile` type |
| `aiNode.ts` | 1355 | Same `mediaType` property error |
| `http.ts` | 159 | `clerkId` does not exist in `fulfillDiamondPurchase` args type |
| `http.ts` | 166 | `markGenerationRefunded` does not exist on generation mutations |

---

## 4. Performance Concerns

| # | Issue | Severity | Details |
|---|-------|----------|---------|
| P-1 | **WASM bundle size** | HIGH | `skiko.wasm` = 8.25 MiB, `HomeDecorAI-app.wasm` = 5.44 MiB, `app.js` = 530 KiB. Total ~14 MiB compressed. First load will be slow on mobile networks. Consider code splitting or lazy loading. |
| P-2 | **Image asset weight** | MEDIUM | 150+ `.webp` images in composeResources/drawable. Compose Resources bundles all into the WASM binary. Consider loading discover images from CDN at runtime instead. |
| P-3 | **No lazy loading for wizard steps** | LOW | All 16 wizard step composables are compiled into the binary. Only 4-6 are needed per tool. |
| P-4 | **Deprecated icon APIs** | LOW | ~10 deprecated `Icons.Rounded.*` references should migrate to `Icons.AutoMirrored.Rounded.*` for correctness. |
| P-5 | **Gradle configuration time** | LOW | `wasmJsNpmAggregated` resolved at configuration time (known Gradle scalability issue). |

---

## 5. Security Concerns

| # | Severity | Issue | Remediation |
|---|----------|-------|-------------|
| S-1 | **CRITICAL** | Subscription spoofing via `setViewerPlanFromRevenueCat` | Replace with webhook-only path. Validate against RevenueCat API server-side. |
| S-2 | **CRITICAL** | Diamond purchase spoofing via `fulfillDiamondPurchase` | Remove client-callable mutation. Fulfill only through RevenueCat webhook. |
| S-3 | **CRITICAL** | `.env.local` committed to git | `git rm --cached .env.local`. Rotate all keys. |
| S-4 | **HIGH** | CORS `anyHost()` | Replace with specific allowed origins. |
| S-5 | **HIGH** | No CSP headers | Add CSP via `vercel.json` and Ktor. |
| S-6 | **HIGH** | No rate limiting on AI endpoints | Add per-user rate limit table. |
| S-7 | **HIGH** | No idempotencyKey from client | Add UUID per generation attempt. |
| S-8 | **MEDIUM** | Guest auth spoofable | Use Convex anonymous auth or signed tokens. |
| S-9 | **MEDIUM** | Prompt injection via `customPrompt` | Add 500-char max length + content moderation. |
| S-10 | **MEDIUM** | Webhook auth fail-open | Fail closed if env var not configured. |
| S-11 | **MEDIUM** | `getGenerationByOrderId` public | Require auth + ownership check. |
| S-12 | **MEDIUM** | Gemini API key in URL query param | Pass via `x-goog-api-key` header. |
| S-13 | **LOW** | Gson `disableHtmlEscaping()` | Remove unless needed. |
| S-14 | **LOW** | `X-User-Id` CORS header | Validate server-side or remove. |

---

## 6. Mobile Concerns

| # | Issue | Severity | Details |
|---|-------|----------|---------|
| MO-1 | **Footer hidden behind keyboard** | HIGH | `WizardFooter` and `SharedPaywallSheet` CTA lack `WindowInsets.ime`. Buttons invisible when mobile keyboard is open. |
| MO-2 | **Validation hint overlaps content on short viewports** | MEDIUM | On viewports <500dp, the animated validation hint pushes content up. |
| MO-3 | **FlowRow chips too narrow on small phones** | LOW | On <320dp viewports, wizard option chips shrink to ~60dp, reducing readability. |
| MO-4 | **WASM bundle size on mobile** | HIGH | 14 MiB total download. On 3G, this is 30+ seconds. Consider CDN delivery + service worker caching. |
| MO-5 | **Double bottom padding on tools screen** | MEDIUM | `NavBarReservation` + Scaffold contentPadding creates excessive dead space. |
| MO-6 | **Preview dialog has no error fallback** | LOW | `SharedDiscoverScreen.kt:970` uses `NetworkImage` directly with no error state if URL fails. |
| MO-7 | **Viewport meta missing `maximum-scale`** | INFO | Optional: Add `maximum-scale=1.0` to prevent unintended pinch-zoom. |

---

## 7. Final Publish Recommendation

### VERDICT: DO NOT PUBLISH

The web production build now compiles and produces distributable output, which is a **significant improvement** from the broken state at the start of this audit. However, there are **unresolved blockers** that prevent safe publication:

**Must-Fix Before Launch (Blocking):**
1. **3 Critical security vulnerabilities** (C-2, C-3, C-4): Subscription and diamond spoofing allow anyone to get free Pro access and unlimited diamonds. This is an active exploit risk.
2. **Android build broken** (C-1): If Android is part of the release, it cannot compile.
3. **Missing rate limiting** (H-2): Without rate limits, a single attacker can run up Azure AI costs to thousands of dollars.
4. **CORS `anyHost()`** (H-3): Any website can make authenticated requests to your Ktor server.

**Should-Fix Before Launch (High):**
5. **Idempotency key missing** (H-1): Users will get duplicate charges on double-click.
6. **Browser back broken in wizard** (H-6): Poor UX, users lose their place.
7. **No CSP headers** (H-4): XSS vulnerability.

**Can Defer (Medium):**
- Validation hint text (M-1), contrast issues (M-5), mobile keyboard insets (M-4), prompt length limits (M-6), stale generation cleanup (M-10).

**Positive Findings:**
- Core wizard flows are logically correct (step counts, tool-specific labels)
- Diamond purchase dedup by `transactionId` is properly implemented
- Daily diamond claim rate-limiting is correctly serialized by Convex transactions
- Body overflow/scrolling is properly handled
- Image card aspect ratios and crop are well-implemented
- Grid column breakpoints adapt properly across viewports
- Resource file organization (composeResources/drawable) is clean and consistent

**Estimated Effort to Clear Blockers:** 2-3 days for a developer familiar with the codebase.
