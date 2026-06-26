# HomeDecor AI Web App Context Summary

## Project Goal

HomeDecor AI started as an Android Jetpack Compose app for AI home design. The goal now is to create a web version to test whether users will visit, understand, and use the product before investing more time in a full production launch.

The web version should not simply be the Android app stretched into a browser. It should become a proper web product experience while keeping the creation wizard simple and app-like where useful.

## Recommended Product Direction

HomeDecor AI should feel like a friendly, smart, premium AI home design tool.

The web app should feel closer to products like Canva, PhotoRoom, RoomGPT, Interior AI, or CapCut Web than to a social feed like Twitter/Facebook.

The best structure is a hybrid:

- Desktop web: professional web layout with header navigation, hero section, examples, upload CTA, and responsive grids.
- Mobile web: closer to the Android app experience.
- Creation flow: step-by-step wizard that works well on both desktop and mobile.

## Visual Style

Use Material Design 3 Expressive.

Preferred palette:

- Background: warm off-white.
- Primary: deep teal.
- Selected states: soft mint.
- Diamonds/rewards: soft gold.
- Warning/destructive: soft coral.
- Text: warm dark charcoal.

Avoid:

- Black/purple-heavy paywall design.
- Gray placeholder blocks.
- Stretched mobile layouts.
- Generic app-copy such as "Explore this tool to enhance your space."
- Overpromising copy like "unlimited" or "4K" unless truly supported.

## Current Web Version Problems

The current website at `http://localhost:8081/` opens directly into Tools. It looks unfinished because it is mostly a mobile app UI copied into the browser.

Main issues:

- No real landing/home section.
- No strong product headline or before/after hero.
- Desktop layout feels wrong.
- Tool cards are huge and empty.
- Tool cards use gray placeholders instead of real visuals.
- Tool names are generic, such as "Room-vision" and "Style-transfer."
- Tool descriptions are generic.
- Bottom navigation is used on desktop, which feels like a mobile clone.
- Desktop should use a top navigation or sidebar.
- "Try this" buttons do not feel premium.
- Tool click flow is not properly connected.
- Discover uses placeholder data instead of organized real categories.
- Upgrade/paywall is too mobile-first and not web-conversion focused.
- Profile/Settings are acceptable conceptually but need desktop polish.
- My Board should exist in web navigation.

## Desired Web Navigation

Desktop navigation should include:

- HomeDecor AI logo/name
- Tools
- Discover
- Upgrade
- My Board
- Profile
- Diamond balance

Mobile/tablet can keep bottom navigation.

## Desired Landing/Home Section

The desktop web app should start with a real hero section:

Headline idea:

`Redesign any room with AI`

Supporting copy idea:

`Upload a photo and explore interior, exterior, garden, and wall design ideas in seconds.`

Primary CTA:

`Upload your room`

Secondary CTA:

`Explore tools`

The hero should include a large before/after visual or slider.

## Tools Page Requirements

Use real tool names:

- Interior Design
- Exterior Design
- Garden Design
- Smart Wall Paint
- Floor Design
- Layout Makeover
- Replace Furniture
- Reference Style

Each tool card should include:

- Real visual/image background or polished generated placeholder.
- Readable gradient overlay.
- Short clear title.
- Short useful description.
- Premium "Try this" button.
- Hover/press interaction on web.
- Responsive grid on desktop: 2 or 3 columns depending on width.
- Single column on mobile.

Do not stretch one card across the whole desktop width.

## Discover Page Requirements

Keep tabs:

- Interior
- Architecture
- Landscape

Use underline selected tab style.

Category groups:

Interior:

- Kitchen
- Living Room
- Bedroom
- Bathroom
- Office
- Dining Room

Architecture:

- Modern House
- Classic House
- Apartment
- Villa
- Cabin

Landscape:

- Garden
- Patio
- Pool Area
- Rooftop
- Balcony

Remove category descriptions under titles. Images should be the focus.

Avoid repeated labels like:

`Kitchen 1, Kitchen`

Use natural accessibility labels like:

`Kitchen inspiration image`

## Creation Flow Requirements

When a user clicks "Try this," open a web-friendly creation wizard.

Expected flow:

1. Upload image.
2. Choose room/type or tool options.
3. Choose style/options.
4. Review and generate/preview.

Web-specific requirements:

- Use browser file upload.
- Add drag-and-drop upload area on desktop.
- Add "Choose image" button.
- Optional "Try with example."
- If backend generation is not ready, show a polished placeholder result or waitlist state.
- Use Material Design 3 Expressive progress indicator.
- Remove Android-only concepts like camera permissions on web.
- Add loading, disabled, success, and error states.

## Upgrade / Paywall Requirements

The web Upgrade page should feel like a real pricing/conversion page, not just a mobile paywall sheet.

Recommended headline:

`Unlock Your Full Design Studio`

Recommended subtitle:

`Create more room makeovers, explore premium tools, and export cleaner results.`

Requirements:

- Strong before/after hero.
- Plan cards on desktop.
- Clear CTA: "Start Pro" or "Join waitlist."
- If web payment is not ready, use "Join waitlist" honestly.
- Avoid risky claims like "unlimited" unless true.
- Benefits with icons:
  - More generations
  - Premium styles
  - Cleaner exports
  - No watermark
  - Design history
- Trust text:
  `Secure checkout. Cancel anytime.`

## Profile / Settings / My Board Requirements

My Board:

- Add to web navigation.
- Do not leave empty.
- If signed out, show polished empty state with blurred/sample saved designs.
- CTA: `Sign in to save your designs.`

Profile:

- Show account status, diamonds, plan, and saved designs.
- Signed-out profile should not feel empty.

Settings:

- Group into sections:
  - Account
  - App
  - Purchases
  - Support
  - Legal
- Hide Delete Account if signed out.
- On desktop, show as a clean panel/modal or page, not only mobile full-screen.

## Copywriting Rules

Tone should be:

- Clear
- Warm
- Confident
- Friendly
- Smart
- Premium
- Not childish
- Not overhyped

Avoid:

- Generic placeholder text.
- Repeated words.
- Awkward labels.
- Claims that are not true.

## Animation / Interaction Ideas

Add subtle micro-interactions:

- Card hover lift.
- Button press state.
- Tab underline slide.
- Image preview fade.
- Upload drag highlight.
- Wizard step transition.
- Paywall plan selection animation.
- Diamond/reward sparkle only where relevant.

Respect reduced motion where possible.

## Important Workflow Note

This project is an experiment folder:

`C:\Users\LENOVO\Desktop\HomeDecor AI (Web-App)`

The original Android app should stay safe:

`C:\Users\LENOVO\Desktop\HomeDecor AI (App)`

Do web conversion and experiments in the Web-App folder, not the original Android folder.

## Current Review Conclusion

The web version can become good, but it needs a proper web-first redesign. The most important fix is to stop treating desktop web as a stretched mobile app. Build a real web shell, landing area, responsive tool grid, web creation flow, and web pricing page.
