package com.ismail.homedecorai

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.StringRes
import com.ismail.homedecorai.R
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Comprehensive error classification for the app
 */
enum class AppErrorKind {
    // Network errors
    Offline,
    Timeout,

    // Resource errors
    Limit,
    Mask,
    ImagePreparation,

    // Generation errors
    Generation,

    // Validation errors
    ValidationError,
    RequiredFieldMissing,
    InvalidInput,

    // Purchase/store errors
    PurchaseSync,
    PaymentFailed,
    SubscriptionExpired,

    // System errors
    Unknown,
}

class AppRecoverableException(
    val kind: AppErrorKind,
    cause: Throwable? = null,
) : IOException(kind.name, cause)

fun Context.hasUsableNetwork(): Boolean {
    val connectivity = getSystemService(ConnectivityManager::class.java) ?: return true
    val network = connectivity.activeNetwork ?: return false
    val capabilities = connectivity.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

fun Throwable.toAppErrorKind(context: Context? = null): AppErrorKind {
    if (this is AppRecoverableException) return kind
    if (context != null && !context.hasUsableNetwork()) return AppErrorKind.Offline
    if (this is TimeoutCancellationException || this is SocketTimeoutException || this is InterruptedIOException) {
        return AppErrorKind.Timeout
    }
    if (this is UnknownHostException) return AppErrorKind.Offline

    val normalized = message.orEmpty().lowercase()
    return when {
        normalized.contains("timed out") ||
            normalized.contains("timeout") ||
            normalized.contains("deadline") -> AppErrorKind.Timeout

        normalized.contains("unable to resolve host") ||
            normalized.contains("failed to connect") ||
            normalized.contains("connection reset") ||
            normalized.contains("connection refused") ||
            normalized.contains("network") ||
            normalized.contains("offline") -> AppErrorKind.Offline

        normalized.contains("mask") -> AppErrorKind.Mask

        normalized.contains("jpg") ||
            normalized.contains("jpeg") ||
            normalized.contains("png") ||
            normalized.contains("decode") ||
            normalized.contains("image") -> AppErrorKind.ImagePreparation

        normalized.contains("diamond") ||
            normalized.contains("credit") ||
            normalized.contains("limit") ||
            normalized.contains("subscribe") -> AppErrorKind.Limit

        normalized.contains("revenuecat") ||
            normalized.contains("purchase") ||
            normalized.contains("billing") ||
            normalized.contains("google play") -> AppErrorKind.PurchaseSync

        normalized.contains("convex") ||
            normalized.contains("backend") ||
            normalized.contains("api") ||
            normalized.contains("azure") ||
            normalized.contains("openai") ||
            normalized.contains("upload") -> AppErrorKind.Generation

        else -> AppErrorKind.Unknown
    }
}

fun rawServiceMessageToKind(context: Context, rawMessage: String?): AppErrorKind {
    if (!context.hasUsableNetwork()) return AppErrorKind.Offline
    val normalized = rawMessage.orEmpty().lowercase()
    return when {
        normalized.contains("timed out") || normalized.contains("timeout") -> AppErrorKind.Timeout
        normalized.contains("network") ||
            normalized.contains("offline") ||
            normalized.contains("connection") ||
            normalized.contains("unable to resolve host") -> AppErrorKind.Offline
        else -> AppErrorKind.Unknown
    }
}

@StringRes
fun AppErrorKind.generationMessageRes(): Int = when (this) {
    AppErrorKind.Offline -> R.string.error_offline_retry
    AppErrorKind.Timeout -> R.string.error_timeout_retry
    AppErrorKind.Limit -> R.string.no_diamonds_recharge
    AppErrorKind.Mask -> R.string.mark_area_before_generate
    AppErrorKind.ImagePreparation -> R.string.image_prepare_failed
    AppErrorKind.ValidationError -> R.string.validation_error_generic
    AppErrorKind.RequiredFieldMissing -> R.string.required_field_missing
    AppErrorKind.InvalidInput -> R.string.invalid_input_error
    else -> R.string.generation_failed_retry
}

@StringRes
fun AppErrorKind.purchaseSyncMessageRes(fallback: Int): Int = when (this) {
    AppErrorKind.Offline -> R.string.purchase_sync_offline
    AppErrorKind.Timeout -> R.string.purchase_sync_timeout
    AppErrorKind.PaymentFailed -> R.string.purchase_offline_retry
    AppErrorKind.SubscriptionExpired -> R.string.restore_failed
    else -> fallback
}

@StringRes
fun AppErrorKind.purchaseAttemptMessageRes(fallback: Int): Int = when (this) {
    AppErrorKind.Offline -> R.string.purchase_offline_retry
    AppErrorKind.Timeout -> R.string.purchase_timeout_retry
    AppErrorKind.PaymentFailed -> R.string.purchase_offline_retry
    AppErrorKind.SubscriptionExpired -> R.string.restore_failed
    else -> fallback
}

@StringRes
fun AppErrorKind.storeMessageRes(fallback: Int): Int = when (this) {
    AppErrorKind.Offline -> R.string.store_offline_retry
    AppErrorKind.Timeout -> R.string.store_timeout_retry
    AppErrorKind.PaymentFailed -> R.string.store_offline_retry
    else -> fallback
}
