package com.ismail.homedecorai

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast

actual fun platformName(): String = "Android ${android.os.Build.VERSION.SDK_INT}"

actual fun hasNetworkConnectivity(): Boolean {
    val context = getApplicationContext()
    val connectivity = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: return true
    val network = connectivity.activeNetwork ?: return false
    val capabilities = connectivity.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

actual fun openUrl(url: String) {
    val context = getApplicationContext() ?: return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

actual fun showToast(message: String) {
    val context = getApplicationContext() ?: return
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

actual fun getScreenWidthDp(): Int {
    val context = getApplicationContext() ?: return 360
    return (context.resources.displayMetrics.widthPixels /
        context.resources.displayMetrics.density).toInt()
}

actual fun setpageTitle(title: String) {
    // No-op on Android: activity title is managed by the Activity itself
}

actual fun pushHistoryState(path: String, title: String) {
    // No-op on Android: navigation is handled by Compose Navigation
}

actual fun replaceHistoryState(path: String, title: String) {
    // No-op on Android: navigation is handled by Compose Navigation
}

actual fun isReducedMotionEnabled(): Boolean {
    val context = getApplicationContext() ?: return false
    return try {
        val resolver = context.contentResolver
        val value = android.provider.Settings.Global.getFloat(
            resolver,
            android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        )
        value == 0f
    } catch (_: Exception) {
        false
    }
}

actual fun announceToScreenReader(message: String) {
    // On Android, Compose semantics handles screen reader announcements
}

actual fun getCurrentPathname(): String = ""

actual fun subscribeToNavigationChanges(onNavigate: (String) -> Unit): () -> Unit = { }

private fun getApplicationContext(): Context? {
    return try {
        val activityThread = Class.forName("android.app.ActivityThread")
        val currentApplication = activityThread.getMethod("currentApplication").invoke(null)
        currentApplication as? Context
    } catch (_: Exception) {
        null
    }
}
