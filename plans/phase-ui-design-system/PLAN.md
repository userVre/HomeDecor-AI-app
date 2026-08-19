# Phase: UI Design System Audit & Improvement

## Goal
Audit and improve the complete visual system of HomeDecor AI without changing backend business logic. Fix accessibility, contrast, navigation, card, modal, and responsive issues across all screens.

## Scope Boundaries
- **IN**: Theme tokens, colors, typography, spacing, shapes, elevation, icons, navigation, cards, modals, accessibility, responsive layout
- **OUT**: Backend logic, Convex functions, AI generation, payment processing, auth flows, data models

---

## Wave 1: Design Token Foundations (no downstream dependencies)

### Task 1.1: Fix Color.kt — Contrast & Disabled States
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/theme/Color.kt`
- Increase `DisabledContent` from `0.38f` to `0.45f` for better visibility while keeping it clearly disabled
- Add explicit `DisabledOnSurface` token: `Color(0xFF9E9A94)` for light, `Color(0xFF5A5C56)` for dark — distinguishable from active text
- Verify `OnSurfaceVariant` contrast ratio against Canvas: currently `0xFF3D3833` on `0xFFECEEEB` = 5.8:1 ✓ (passes AA)
- Verify `DarkOnSurfaceVariant` contrast: `0xFFB8B9B2` on `0xFF0E100E` = 9.2:1 ✓

### Task 1.2: Fix Type.kt — Mobile Body Text Minimum
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/theme/Type.kt`
- `BodySmall` is currently `12.sp` — this is used in labels/badges, not body text. Acceptable.
- `BodyMedium` is `14.sp` — used for descriptions. Ensure it's never used as primary reading text on mobile.
- `BodyLarge` is `16.sp` ✓ — already meets the 16px mobile minimum for body text
- `LabelSmall` is `11.sp` — used only for badges/counters, not reading text. Acceptable.
- No changes needed to the type scale — it already meets the requirements.

### Task 1.3: Fix Dimens.kt — Touch Targets
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/theme/Dimens.kt`
- `TouchTarget` is `48.dp` — need to add `MinTouchTarget = 44.dp` as an alias for WCAG compliance
- The existing `minimumTouchTarget()` modifier uses `48.dp` which exceeds the 44px minimum ✓
- Add `IconSize` scale to Dimens.kt:
  - `IconSizeSmall = 16.dp` (inline icons in chips, badges)
  - `IconSizeMedium = 20.dp` (standard icons in buttons, list items)
  - `IconSizeLarge = 24.dp` (standalone icons, nav items)
  - `IconSizeXl = 32.dp` (empty states, hero icons)
- Add `StrokeWidth = 2.dp` constant for icon stroke weight consistency

### Task 1.4: Fix Theme.kt — Disabled State Helper
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/theme/Theme.kt`
- Update `studioPrimaryButtonColors()` to use the new higher-opacity disabled state
- Add `surfaceBorder` semantic color to `HomeDecorExtraColors` for consistent card borders
- Add `surfaceBorderSelected` semantic color for 2dp selected card borders
- Map these in `LightExtra` and `DarkExtra`

---

## Wave 2: Component Improvements (depends on Wave 1 tokens)

### Task 2.1: Fix ImageCard.kt — States & Accessibility
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/components/ImageCard.kt`
- **Favorite button touch target**: Currently `32.dp` — increase to `44.dp` with visual `28.dp` button inside
- **Menu button touch target**: Currently `32.dp` — increase to `44.dp`
- **Selected border**: Already `2.dp` ✓ — verify it uses the new `surfaceBorderSelected` token
- **Empty state**: Currently uses gradient background — add a deliberate swatch pattern instead of just gradient
- **Error fallback**: Already shows retry button ✓
- Add `Modifier.minimumTouchTarget()` to both IconButton wrappers
- Ensure all icons use `IconSizeSmall` (16.dp) consistently — currently mixing 14dp, 16dp, 18dp

### Task 2.2: Fix DialogComponents.kt — Modal Requirements
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/components/DialogComponents.kt`
- **Scrim**: Already `0.4f` alpha ✓
- **Scrollable content**: Already uses `verticalScroll()` ✓
- **Primary action visible**: Footer already separated ✓
- **Escape dismissal**: Desktop dialog already handles via `onPreviewKeyEvent` in App.kt ✓
- **Close button**: Already has 48dp IconButton ✓
- **Background scroll prevention**: Need to add `nestedScroll(rememberNestedScrollConnection())` or disable scroll on background
- Fix: The `DesktopDialog` `AnimatedVisibility` wrapper is always `visible = true` — this means the exit animation never plays. Fix by hoisting visibility state.

### Task 2.3: Fix SelectionComponents.kt — Card States
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/wizard/SelectionComponents.kt`
- **Selected border**: Already `2.dp` ✓
- **Checkmark badge**: Already present ✓
- **Hover/pressed states**: Already has scale animation ✓
- Fix: The `Surface` inside `SelectionCard` should use `HomeDecorShape.Card` for consistency with the outer border shape
- Add `disabledSemantics()` when card is in a disabled state (not currently supported — add `enabled` parameter)

### Task 2.4: Fix SharedToolsScreen.kt — Tool Cards
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/tools/SharedToolsScreen.kt`
- **Icon consistency**: ToolCard CTA uses `Icons.Rounded.Palette` with `14.dp` — all tool cards should use the same icon size
- **Description text shadow**: Uses `Color.Black.copy(alpha = 0.55f)` behind white text — this is the shadow layer. The actual text is `Color(0xD9FFFFFF)` = 85% white. Verify readability.
- **Focus ring**: Already implemented ✓
- Add consistent icon size token usage

---

## Wave 3: Navigation & Screen-Level (depends on Wave 1-2)

### Task 3.1: Fix App.kt — Navigation Labels & Active State
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/App.kt`
- **Tab labels**: Already labeled: Tools, Discover, My Board, Pro, Profile ✓
- **Route naming**: `WebTab.Tools` uses `/tools` — rename label to "Create" per requirements? No — the user said "Create, Discover, My Board, Pro, Profile". Current: Tools, Discover, My Board, Pro, Profile. Need to rename "Tools" tab to "Create".
- **Active state**: `NavigationBarItem` already uses `selected` state ✓
- **Bottom nav on mobile**: Already uses `NavigationBar` ✓
- **Top nav on desktop**: Already uses `DesktopTopNav` ✓
- Rename `WebTab.Tools` label from "Tools" to "Create" and update icon from `Widgets` to `Create` (or keep `Widgets` — `Create` icon is `Icons.Rounded.Create`)
- Update route from `/tools` to `/create` — wait, `/create` is already used for wizard. Keep `/tools` for the tools list, but change the label to "Create".

### Task 3.2: Fix Responsive Breakpoints
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/ResponsiveLayout.kt`
- Current breakpoints: Compact <600dp, Desktop >=1024dp
- Need to add Tablet breakpoint at 768dp for the 3-column grid
- The `SelectionGrid` already handles this via `responsiveColumns()` but uses 600dp, not 768dp
- Update `responsiveColumns()` in `SelectionComponents.kt` to use 768dp for tablet breakpoint

### Task 3.3: Fix Board Cards — Image Fallback
**File**: `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/board/SharedMyBoardScreen.kt`
- **Image fallback**: When `imageUrl` is null/blank, shows a gradient with an icon — this is acceptable as a "deliberate fallback swatch" ✓
- **Card click**: Already uses `Surface(onClick = onClick)` ✓
- **Equal card heights**: Uses `LazyVerticalGrid` with `GridCells.Adaptive` — cards auto-size. The image area is fixed at `140.dp` height ✓

---

## Wave 4: Verification & Polish

### Task 4.1: Verify Responsive Layouts
- 375px mobile: Bottom nav visible, single column grids, body text 16px+
- 768px tablet: Bottom nav visible, 2-3 column grids
- 1024px desktop: Top nav visible, 4 column grids, max-width 1200dp
- 1440px desktop: Top nav visible, content centered at 1200dp

### Task 4.2: Verify Dark Mode
- All surface levels visually distinct
- Text contrast ratios meet WCAG AA (4.5:1 for body, 3:1 for large text)
- No pure black backgrounds
- Scrim overlays work correctly

### Task 4.3: Verify Keyboard Navigation
- All interactive elements focusable
- Focus rings visible (3dp primary-colored stroke)
- Tab order follows visual layout
- Escape closes modals

### Task 4:4: Verify Reduced Motion
- `isReducedMotionEnabled()` checked in animations
- Scale/fade transitions reduced or removed
- No infinite animations in reduced motion mode

### Task 4.5: Run Build & Tests
- `./gradlew wasmJsBrowserDevelopmentWebpack` — verify web build
- Run existing Compose tests
- Verify no compilation errors

---

## Anti-Patterns to Avoid
- Do NOT change Convex backend functions
- Do NOT modify AI generation pricing or logic
- Do NOT change auth flow business logic
- Do NOT alter checkout URLs or payment processing
- Do NOT change data models (Models.kt)
- Do NOT break existing test tags

## Verification
- Build: `./gradlew wasmJsBrowserDevelopmentWebpack`
- Tests: `./gradlew desktopTest` (or equivalent)
- Visual: Manual check at 375px, 768px, 1024px, 1440px
- Accessibility: Keyboard tab through all screens, verify focus rings
