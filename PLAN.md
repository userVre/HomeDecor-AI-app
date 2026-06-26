# Redesign Web Shell & Landing Page — Implementation Plan

## Objective
Redesign the desktop web shell from a left sidebar to a top horizontal navigation bar, create a real landing/home page with a strong hero section, implement deep-linkable URL routing, and keep mobile bottom navigation only for mobile/tablet.

---

## Architecture Overview

### Current State
- **Framework**: Kotlin Multiplatform → Compose Multiplatform → WasmJs (WebAssembly)
- **Navigation**: State-driven `mutableStateOf<WebTab>` — no URL integration
- **Desktop layout**: Left sidebar (200dp wide) with vertical nav items
- **Mobile layout**: Material 3 bottom navigation bar
- **Landing page**: None — app opens directly to Tools tab
- **Strings.kt**: Landing page copy already defined (lines 13–27) but unused

### Target State
- **URL routing**: Path-based deep links (`/home`, `/tools`, `/discover`, etc.) via `history.pushState`
- **Desktop layout**: Top horizontal navbar with logo, nav links, diamond balance chip
- **Mobile layout**: Bottom navigation (unchanged, only for <900dp)
- **Landing page**: New `Home` tab with hero headline, animated before/after, feature cards, CTAs
- **All sections deep-linkable**: Each tab has a URL path

---

## Step-by-Step Implementation

### Step 1: Add Browser History Interop Functions

**Files to modify:**
- `app/src/commonMain/kotlin/com/ismail/homedecorai/PlatformAbstractions.kt` — add `expect` declarations
- `app/src/wasmJsMain/kotlin/com/ismail/homedecorai/Platform.kt` — add `actual` implementations

**What to add:**

In `PlatformAbstractions.kt`:
```kotlin
expect fun pushRoute(path: String)
expect fun replaceRoute(path: String)
expect fun getInitialRoute(): String
expect fun onRouteChange(callback: (String) -> Unit): () -> Unit
```

In `Platform.kt` (WasmJs actuals):
```kotlin
@JsFun("(path) => window.history.pushState({}, '', path)")
private external fun pushState(path: String)

@JsFun("(path) => window.history.replaceState({}, '', path)")
private external fun replaceState(path: String)

@JsFun("() => window.location.pathname")
private external fun getPathname(): String

@JsFun("(cb) => { const handler = (e) => { cb(window.location.pathname); }; window.addEventListener('popstate', handler); return handler; }")
private external fun addPopstateListener(cb: JsFunction)

actual fun pushRoute(path: String) = pushState(path)
actual fun replaceRoute(path: String) = replaceState(path)
actual fun getInitialRoute(): String = getPathname()
actual fun onRouteChange(callback: (String) -> Unit): () -> Unit {
    val handler: JsFunction = { _: JsAny? -> callback(getPathname()) }
    // ... wire up popstate listener
}
```

> **Note**: Kotlin/WasmJs uses `@JsFun` for JS interop. The exact interop syntax may need adjustment based on Kotlin 2.3.21 WasmJs interop patterns. We'll use the `@JsFun` annotation with arrow functions.

---

### Step 2: Create Route Configuration

**New file:** `app/src/commonMain/kotlin/com/ismail/homedecorai/Routing.kt`

```kotlin
enum class Route(val path: String, val label: String, val icon: ImageVector) {
    Home("/home", "Home", Icons.Rounded.Home),
    Tools("/tools", "Tools", Icons.Rounded.Widgets),
    Discover("/discover", "Discover", Icons.Rounded.Explore),
    Board("/board", "My Board", Icons.Rounded.Dashboard),
    Upgrade("/upgrade", "Pro", Icons.Rounded.Stars),
    Profile("/profile", "Profile", Icons.Rounded.Person);

    companion object {
        fun fromPath(path: String): Route = entries.find { it.path == path } ?: Home
    }
}
```

**Design decisions:**
- `Home` is the new default route (replaces `Tools` as the landing)
- All routes have a `/path` for deep linking
- The old `WebTab` enum is replaced by `Route`

---

### Step 3: Refactor App.kt — Integrate URL Routing

**File to modify:** `app/src/commonMain/kotlin/com/ismail/homedecorai/App.kt`

**Changes:**

1. **Replace `WebTab` enum** with the new `Route` enum
2. **Read initial route from URL** on composition start:
   ```kotlin
   val initialRoute = remember { Route.fromPath(getInitialRoute()) }
   var selectedRoute by remember { mutableStateOf(initialRoute) }
   ```
3. **Sync navigation to URL** — when `selectedRoute` changes, call `pushRoute(selectedRoute.path)`
4. **Listen for browser back/forward** — register `onRouteChange` listener that updates `selectedRoute`
5. **Update the `when` block** to handle `Route.Home` → `HomeScreen()` (new)
6. **Update desktop layout** — pass `selectedRoute` and `onSelectRoute` to the new `DesktopTopBar`
7. **Update mobile layout** — pass `selectedRoute` and `onSelectRoute` to `WebBottomBar`

**Key interaction flow:**
- User clicks "Discover" in top nav → `selectedRoute = Route.Discover` → `pushRoute("/discover")` → content shows DiscoverScreen
- User clicks browser back button → popstate fires → `selectedRoute = Route.fromPath(pathname)` → content updates

---

### Step 4: Create Desktop Top Navigation Bar

**File to modify:** `app/src/commonMain/kotlin/com/ismail\homedecorai/App.kt`

**Replace `DesktopAppLayout`** — remove the left sidebar, replace with:

```kotlin
@Composable
private fun DesktopTopBar(
    selectedRoute: Route,
    onSelectRoute: (Route) -> Unit,
    diamonds: Int,
    isPro: Boolean,
    onOpenDiamonds: () -> Unit,
) {
    Surface(
        color = HomeDecorColors.Paper,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().height(64.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Logo / brand
            Text("HomeDecor AI", ..., fontWeight = FontWeight.Bold, color = HomeDecorColors.Accent)

            Spacer(Modifier.weight(1f))

            // Nav links (Home, Tools, Discover, My Board, Upgrade)
            Route.entries.filter { it != Route.Profile }.forEach { route ->
                DesktopNavLink(route, selectedRoute == route) { onSelectRoute(route) }
            }

            Spacer(Modifier.weight(1f))

            // Diamond balance chip
            DiamondBalanceChip(diamonds, isPro, onOpenDiamonds)

            // Profile icon
            IconButton(onClick = { onSelectRoute(Route.Profile) }) {
                Icon(Icons.Rounded.Person, ...)
            }
        }
    }
}
```

**Layout structure (desktop ≥900dp):**
```
┌──────────────────────────────────────────────────────────┐
│ [Logo]     Home  Tools  Discover  Board  Pro    [♦ 150] [👤] │  ← 64dp top bar
├──────────────────────────────────────────────────────────┤
│                                                          │
│                    Main Content Area                      │
│                    (scrollable)                           │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

**`DesktopNavLink` composable:**
```kotlin
@Composable
private fun DesktopNavLink(
    route: Route,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val color = if (isSelected) HomeDecorColors.Accent else HomeDecorColors.InkSoft
    TextButton(onClick = onClick) {
        Text(
            route.label,
            color = color,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
        )
        if (isSelected) {
            // Bottom indicator bar
            Box(
                Modifier.fillMaxWidth().height(2.dp)
                    .background(HomeDecorColors.Accent, RoundedCornerShape(1.dp))
                    .align(Alignment.Bottom)
            )
        }
    }
}
```

**`DiamondBalanceChip` composable:**
```kotlin
@Composable
private fun DiamondBalanceChip(diamonds: Int, isPro: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = HomeDecorShape.Button,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(Icons.Rounded.Diamond, ..., size = 16.dp, tint = HomeDecorExtra.diamondAccent)
            Text(
                if (isPro) "PRO" else "$diamonds",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
```

---

### Step 5: Create Landing/Home Page

**New file:** `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/home/SharedHomeScreen.kt`

**Sections:**

#### 5a. Hero Section
```
┌─────────────────────────────────────────────┐
│                                             │
│      Redesign any room with AI              │  ← headlineLarge, bold, 48dp
│                                             │
│   Upload a photo of any room and watch      │  ← bodyLarge, secondary color
│   it transform. Instant mockups,            │
│   unlimited styles, professional results.   │
│                                             │
│   [Upload your room]  [Explore tools]       │  ← primary + secondary CTAs
│                                             │
│   ✓ Free to start  ✓ No credit card  ✓ Cancel anytime  ← trust badges
│                                             │
│   ┌─────────────────────────────────────┐   │
│   │     Before ←→ After (animated)      │   │  ← before/after visual
│   │     [gradient placeholder image]    │   │
│   └─────────────────────────────────────┘   │
│                                             │
└─────────────────────────────────────────────┘
```

#### 5b. Features Section (3-column grid)
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│ 🤖 AI    │  │ ⚡ Instant │  │ 🎨 Unlimited│
│ Powered  │  │ Results  │  │ Styles   │
│          │  │          │  │          │
│ Smart AI │  │ See your │  │ From     │
│ understands│ │ room in  │  │ minimal  │
│ your space│  │ seconds  │  │ to boho  │
└──────────┘  └──────────┘  └──────────┘
```

#### 5c. Before/After Animated Transition
- A gradient placeholder image (since no real images exist) split into two halves
- An animated divider that oscillates left→right→left every 3 seconds
- Labels "Before" and "After" that follow the divider
- Uses `animateFloatAsState` with `infiniteTransition`
- Color: left half is muted/warm tones (before), right half is vibrant teal tones (after)

**Implementation approach for before/after:**
```kotlin
@Composable
fun BeforeAfterVisual() {
    val infiniteTransition = rememberInfiniteTransition(label = "ba")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "divider",
    )
    // Clip left "before" half and right "after" half based on progress
    // Render divider line at progress position
    // Show "Before" / "After" labels
}
```

#### 5d. Desktop Layout for Home Screen
The home screen should be scrollable with a max-width container (1100dp) for readability:
- Hero takes full width within container
- Features in 3-column grid on desktop, 1-column on mobile
- Generous vertical padding (48dp between sections)

---

### Step 6: Update SharedToolsScreen — Remove Internal Header

**File to modify:** `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/tools/SharedToolsScreen.kt`

The diamond balance and "Tools" title currently live in `ToolsHeader`. Since these move to the top nav bar, the `ToolsHeader` should be removed or simplified:
- Remove `ToolsHeader` call from `SharedToolsScreen`
- Remove `onCredits` parameter (now handled by top nav diamond chip)
- The screen becomes a pure grid of tool cards with no header

---

### Step 7: Update Mobile Bottom Navigation

**File to modify:** `app/src/commonMain/kotlin/com/ismail/homedecorai/App.kt`

The `WebBottomBar` composable stays mostly the same but:
- Add `Route.Home` as the first item (with `Icons.Rounded.Home` icon)
- Use `Route` enum instead of `WebTab`
- Keep it only for `!isDesktop` (the existing `rememberIsDesktop()` check)

**Updated bottom bar items (mobile):**
```
Home | Tools | Discover | Board | Profile
```
(5 items max per Material Design guidelines — Pro/Upgrade accessed from elsewhere)

---

### Step 8: Update ResponsiveLayout for Desktop Max Width

**File to modify:** `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/ResponsiveLayout.kt`

The desktop content area needs proper width constraints now that the sidebar is gone:
- Max width: 1200.dp for the main content area
- Center-aligned on the page
- Full height minus the 64dp top bar

---

### Step 9: Add Strings for New Components

**File to modify:** `app/src/commonMain/kotlin/com/ismail/homedecorai/Strings.kt`

The landing page strings already exist (lines 13–27). Add any missing ones:
```kotlin
const val navHome = "Home"
const val homeBeforeLabel = "Before"  // already exists
const val homeAfterLabel = "After"    // already exists
const val homeFeature1Title = "AI-Powered"  // already exists
// ... etc
```

---

### Step 10: Handle Wizard Overlay with URL State

When a tool wizard is active, the URL should reflect the tool context:
- When wizard opens: `pushRoute("/tools/interior")` 
- When wizard closes: `replaceRoute("/tools")`
- Add a `ToolRoute` or handle sub-routes as a state overlay

**Approach:** Keep the wizard as an overlay state (current approach) but update the URL:
```kotlin
var activeWizard by remember { mutableStateOf<ToolItem?>(null) }
// When activeWizard changes:
LaunchedEffect(activeWizard) {
    if (activeWizard != null) {
        pushRoute("/tools/${activeWizard!!.id}")
    } else {
        pushRoute(selectedRoute.path)
    }
}
```

---

## File Change Summary

| File | Action | Description |
|------|--------|-------------|
| `PlatformAbstractions.kt` | Modify | Add `pushRoute`, `replaceRoute`, `getInitialRoute`, `onRouteChange` expect declarations |
| `Platform.kt` (wasmJs) | Modify | Add actual implementations using `@JsFun` JS interop |
| `Routing.kt` | **Create** | New route enum with paths, labels, icons |
| `App.kt` | Modify | Replace `WebTab` with `Route`, integrate URL routing, replace desktop sidebar with top bar, add Home to mobile bottom bar |
| `SharedHomeScreen.kt` | **Create** | New landing page with hero, before/after animation, features, CTAs |
| `SharedToolsScreen.kt` | Modify | Remove `ToolsHeader` (diamond balance moves to top nav) |
| `ResponsiveLayout.kt` | Modify | Update desktop max-width and layout for top-nav design |
| `Strings.kt` | Modify | Add `navHome` string |

---

## Key Design Decisions

1. **Path-based URLs** (`/home`, `/tools`) — cleaner than hash-based, requires SPA fallback in server config
2. **Home as default route** — `/home` is the landing page; `/` redirects to `/home`
3. **Desktop top nav** — horizontal, 64dp height, brand left, nav center, actions right
4. **Diamond chip in top nav** — compact, clickable, shows count or "PRO" label
5. **Profile as icon** — rightmost in top nav, opens profile page
6. **Mobile bottom nav** — 5 items: Home, Tools, Discover, Board, Profile (Upgrade accessed from elsewhere)
7. **Animated before/after** — uses Compose infiniteTransition, oscillating divider, gradient placeholders
8. **No real images** — uses gradient placeholders consistent with existing tool card design
9. **Wizard overlay** — keeps current overlay approach, URL updates for context

---

## UX Compliance Checklist (from ui-ux-pro-max)

- [x] Touch targets ≥48dp (Material)
- [x] Nav items have both icon + label
- [x] Deep linking for all sections
- [x] Predictable back navigation (browser back works)
- [x] Bottom nav ≤5 items
- [x] Visual hierarchy via size/spacing/contrast
- [x] Consistent color tokens (no ad-hoc hex)
- [x] Focus states on interactive elements
- [x] Reduced-motion: before/after animation uses `infiniteTransition` which respects `prefers-reduced-motion`
- [x] No emoji as structural icons (using Material Icons)
- [x] 8dp spacing rhythm maintained

---

## Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| `@JsFun` interop complexity for history API | Test with minimal JS interop first; use `js()` inline if `@JsFun` is tricky |
| Server must return index.html for all routes | Document in deployment: add SPA fallback rule for hosting config |
| Existing screens assume no top bar height | Add 64dp top padding in desktop content area |
| Wizard URL state sync | Use LaunchedEffect to sync wizard state with URL |

---

## Estimated Effort

- **Step 1** (JS interop): Medium — depends on WasmJs interop patterns
- **Step 2** (Route config): Low — simple enum
- **Step 3** (App.kt refactor): High — largest file, most changes
- **Step 4** (Desktop top nav): Medium — new composables
- **Step 5** (Home screen): High — new screen with animations
- **Step 6** (Tools header removal): Low
- **Step 7** (Mobile bottom nav update): Low
- **Step 8** (ResponsiveLayout update): Low
- **Step 9** (Strings): Low
- **Step 10** (Wizard URL): Low-Medium

**Total: ~10 file changes, 2 new files**
