# Plan: Refactor Smart Wall Paint & Floor Design Wizards

## Objective
Refactor the Smart Wall Paint and Floor Design wizard steps to use shared wizard components, improve UX consistency, and fix various issues.

## Files to Modify
1. `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/tools/WebWizardScreen.kt` — Step composables, option data, state
2. `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/wizard/WizardScaffold.kt` — Footer generate button label, content clipping
3. `app/src/commonMain/kotlin/com/ismail/homedecorai/ui/components/ReviewStepComponents.kt` — Generate action labels
4. `app/src/commonMain/kotlin/com/ismail/homedecorai/Strings.kt` — String constants

## Steps

### Step 1: Update String Constants (Strings.kt)
- Change `wizardActionFloor` from `"Apply flooring"` to `"Apply flooring · 10 \uD83D\uDC8E"`
- Change `wizardActionPaint` from `"Apply paint"` to `"Apply paint · 10 \uD83D\uDC8E"`

### Step 2: Add Rug Toggle to WizardState (WebWizardScreen.kt)
- Add `selectedRugOption: Boolean = false` field to `WizardState` data class

### Step 3: Remove Area Rugs from floorStyleOptions (WebWizardScreen.kt)
- Remove `WizardOption("rugs", "Area Rugs")` from `floorStyleOptions()`
- Floor styles become 11 items: Hardwood, Marble, Concrete, Tile, Carpet, Laminate, Vinyl, Bamboo, Stone, Terracotta, Parquet

### Step 4: Refactor MaterialStep to Use Shared SelectionCard (WebWizardScreen.kt)
- Replace `MaterialSwatchCard` usage with `SelectionCard` from `SelectionComponents.kt`
- Add selection count text ("1 of 10 materials selected")
- Use `SelectionGrid` for responsive layout
- Ensure 150-220ms selection animation (already in SelectionCard via spring)

### Step 5: Refactor PaintColorStep to Use Shared SelectionCard (WebWizardScreen.kt)
- Replace `PaintColorSwatch` usage with `SelectionCard` from `SelectionComponents.kt`
- Add selection count text ("1 of 16 colors selected")
- Use `SelectionGrid` for responsive layout

### Step 6: Refactor FloorStyleStep to Use Shared SelectionCard (WebWizardScreen.kt)
- Replace `FloorMaterialCard` usage with `SelectionCard` from `SelectionComponents.kt`
- Add selection count text ("1 of 11 styles selected")
- Add optional rug toggle below the style grid
- Use `SelectionGrid` for responsive layout

### Step 7: Fix Content Clipping Behind Footer (WizardScaffold.kt)
- Remove `.clipToBounds()` from the content Box in WizardScaffold
- This allows scrollable content to render fully without clipping

### Step 8: Update Generate Button Labels (ReviewStepComponents.kt + WizardScaffold.kt)
- `generateActionForTool` already delegates to Strings constants (updated in Step 1)
- Footer generate button in WizardScaffold.kt uses hardcoded text — update to use `generateActionForTool` pattern or the Strings constant

### Step 9: Replace Raw Enum Strings with Human-Readable Labels (WebWizardScreen.kt)
- Update `stepReviewValue()` to return human-readable labels instead of raw IDs
- Add a helper function `labelForMaterialId(id)` and `labelForPaintColorId(id)` and `labelForFloorStyleId(id)`
- Use these in `stepReviewValue()` for Material, PaintColor, and FloorStyle steps

### Step 10: Verify MD3 Icons Only
- All icons currently use `Icons.Rounded.*` from Material Design 3 — verified OK
- `Icons.AutoMirrored.Rounded.ArrowBack` and `ArrowForward` are MD3 — verified OK

## Verification
- Visual: All selection cards use consistent SelectionCard component
- Functional: Next button disabled until valid selection made
- UX: Selection counts visible, inline validation hints shown
- Animation: Selection animations in 150-220ms range (SelectionCard spring)
- Labels: Review step shows "Carrara Marble" not "carrara-marble"
- Generate: Button shows "Apply paint · 10 💎" and "Apply flooring · 10 💎"
- Rug: Area Rugs removed from floor styles, added as optional toggle
- Clipping: Content scrolls fully without being cut off by footer
