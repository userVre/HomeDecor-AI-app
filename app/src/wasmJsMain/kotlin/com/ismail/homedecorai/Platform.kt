package com.ismail.homedecorai

actual fun platformName(): String = "Web (WasmJs)"

actual fun hasNetworkConnectivity(): Boolean = true

@JsFun("(url) => window.open(url, '_blank')")
private external fun openUrlJs(url: String)

actual fun openUrl(url: String) {
    openUrlJs(url)
}

@JsFun("(msg) => window.alert(msg)")
private external fun showToastJs(msg: String)

actual fun showToast(message: String) {
    showToastJs(message)
}

@JsFun("() => Math.floor(window.innerWidth)")
private external fun getScreenWidthDpJs(): Int

actual fun getScreenWidthDp(): Int = getScreenWidthDpJs()

@JsFun("(title) => { document.title = title; }")
private external fun setPageTitleJs(title: String)

actual fun setpageTitle(title: String) {
    setPageTitleJs(title)
}

@JsFun("(path, title) => { history.pushState({ path: path }, title, path); document.title = title; }")
private external fun pushHistoryStateJs(path: String, title: String)

actual fun pushHistoryState(path: String, title: String) {
    pushHistoryStateJs(path, title)
}

@JsFun("(path, title) => { history.replaceState({ path: path }, title, path); document.title = title; }")
private external fun replaceHistoryStateJs(path: String, title: String)

actual fun replaceHistoryState(path: String, title: String) {
    replaceHistoryStateJs(path, title)
}

@JsFun("() => window.matchMedia('(prefers-reduced-motion: reduce)').matches")
private external fun isReducedMotionEnabledJs(): Boolean

actual fun isReducedMotionEnabled(): Boolean = isReducedMotionEnabledJs()

@JsFun("(msg) => { var el = document.getElementById('a11y-announcer'); if (el) { el.textContent = ''; requestAnimationFrame(() => { el.textContent = msg; }); } }")
private external fun announceToScreenReaderJs(msg: String)

actual fun announceToScreenReader(message: String) {
    announceToScreenReaderJs(message)
}

@JsFun("() => window.location.pathname")
private external fun getCurrentPathnameJs(): String

actual fun getCurrentPathname(): String = getCurrentPathnameJs()

@JsFun("""(callback) => {
    var handler = function(event) {
        callback(window.location.pathname);
    };
    window.addEventListener('popstate', handler);
    return function() { window.removeEventListener('popstate', handler); };
}""")
private external fun subscribeToNavigationChangesJs(callback: (String) -> Unit): () -> Unit

actual fun subscribeToNavigationChanges(onNavigate: (String) -> Unit): () -> Unit =
    subscribeToNavigationChangesJs(onNavigate)

@JsFun("() => { history.back(); }")
private external fun goBackJs()

actual fun goBack() {
    goBackJs()
}

@JsFun("() => { try { return localStorage.getItem('hd_dark_theme') || ''; } catch(e) { return ''; } }")
private external fun getDarkThemeJs(): String

actual fun getPersistedDarkTheme(): Boolean {
    return getDarkThemeJs() == "true"
}

@JsFun("(value) => { try { localStorage.setItem('hd_dark_theme', value); } catch(e) {} }")
private external fun setDarkThemeJs(value: String)

actual fun persistDarkTheme(isDark: Boolean) {
    setDarkThemeJs(if (isDark) "true" else "false")
}
