package com.ismail.homedecorai

expect fun openUrl(url: String)

expect fun showToast(message: String)

expect fun getScreenWidthDp(): Int

expect fun setpageTitle(title: String)

expect fun pushHistoryState(path: String, title: String)

expect fun replaceHistoryState(path: String, title: String)

expect fun isReducedMotionEnabled(): Boolean

expect fun announceToScreenReader(message: String)

expect fun getCurrentPathname(): String

expect fun subscribeToNavigationChanges(onNavigate: (String) -> Unit): () -> Unit

expect fun goBack()
