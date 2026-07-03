package com.ismail.homedecorai

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@JsFun("(msg) => { /* production: no console.log */ }")
private external fun logToConsole(msg: String)

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    try {
        ComposeViewport("composeApp") {
            App()
        }
    } catch (_: Throwable) {
        // Error will surface via the app-error UI in index.html
    }
}
