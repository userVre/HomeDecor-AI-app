package com.ismail.homedecorai

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@JsFun("(msg) => console.log(msg)")
private external fun logToConsole(msg: String)

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    logToConsole("HomeDecor AI: main() called")
    try {
        ComposeViewport("composeApp") {
            App()
        }
        logToConsole("HomeDecor AI: ComposeViewport created")
    } catch (e: Throwable) {
        logToConsole("HomeDecor AI ERROR: ${e.message}")
    }
}
