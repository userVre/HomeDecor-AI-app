package com.ismail.homedecorai

import androidx.compose.runtime.mutableStateOf

object ToastState {
    private val _message = mutableStateOf<String?>(null)
    val message: String? get() = _message.value

    private val _isVisible = mutableStateOf(false)
    val isVisible: Boolean get() = _isVisible.value

    fun show(msg: String) {
        _message.value = msg
        _isVisible.value = true
    }

    fun dismiss() {
        _isVisible.value = false
        _message.value = null
    }
}
