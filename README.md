# HomeDecor AI Native

Native Kotlin + Jetpack Compose Android rewrite of the HomeDecor AI mobile app.

The React Native/Expo source app remains in `C:\Users\LENOVO\Desktop\Darkor.AI,ismail\Darkor.ai`.
This folder is the new Android project using:

- Kotlin
- Jetpack Compose
- Material 3 Expressive
- Clerk + Convex integration dependencies
- RevenueCat Android SDK

## Build

```powershell
.\gradlew.bat :app:assembleDebug
```

Runtime configuration is read from Gradle properties or environment variables:

- `HOMED_CLERK_PUBLISHABLE_KEY`
- `HOMED_CONVEX_URL`
- `HOMED_REVENUECAT_ANDROID_API_KEY`
- `HOMED_APP_URL`

The UI is native Compose and intentionally keeps the initial asset set compact to avoid the Expo-era binary bloat.
