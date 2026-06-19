import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use(::load)
    }
}

fun configValue(name: String, fallback: String = ""): String {
    return ((project.findProperty(name) as? String)
        ?: localProperties.getProperty(name)
        ?: System.getenv(name)
        ?: fallback).trim().removeSurrounding("\"")
}

android {
    namespace = "com.ismail.homedecorai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ismail.homedecorai"
        minSdk = 26
        targetSdk = 36
        versionCode = 37
        versionName = "1.1.0-native"

        buildConfigField("String", "CLERK_PUBLISHABLE_KEY", "\"${configValue("HOMED_CLERK_PUBLISHABLE_KEY")}\"")
        buildConfigField("String", "CONVEX_URL", "\"${configValue("HOMED_CONVEX_URL", "https://curious-nightingale-129.convex.cloud")}\"")
        buildConfigField("String", "REVENUECAT_ANDROID_API_KEY", "\"${configValue("HOMED_REVENUECAT_ANDROID_API_KEY")}\"")
        buildConfigField("String", "APP_URL", "\"${configValue("HOMED_APP_URL", "https://homedecor.ai")}\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3") {
        version {
            strictly("1.4.0-alpha18")
        }
    }
    implementation("androidx.compose.material3:material3-android") {
        version {
            strictly("1.4.0-alpha18")
        }
    }
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-text-google-fonts")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    implementation("com.clerk:clerk-convex-kotlin:0.11.0")
    implementation("dev.convex:android-convexmobile:0.8.0@aar") {
        isTransitive = true
    }
    implementation("com.revenuecat.purchases:purchases:10.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
