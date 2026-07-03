plugins {
    kotlin("jvm")
    application
}

group = "com.ismail.homedecorai"
version = "1.0.0"

application {
    mainClass.set("com.ismail.homedecorai.ApplicationKt")
}

repositories {
    mavenCentral()
}

val ktorVersion = "3.1.1"
val kotlinxVersion = "1.10.2"

dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")

    // OkHttp (for Whop API calls)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Gson (for JSON parsing)
    implementation("com.google.code.gson:gson:2.11.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // dotenv (.env file loading)
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
}

kotlin {
    jvmToolchain(17)
}
