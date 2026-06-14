package com.ismail.homedecorai

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.StringRes
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
    GenerationFailed,
    GenerationCancelled,
    
    // Validation errors
    ValidationError,
    RequiredFieldMissing,
    InvalidInput,
    
    // Purchase/store errors
    PurchaseSync,
    Store,
    PaymentFailed,
    SubscriptionExpired,
    
    // System errors
    Unknown,
    InternalError,
}

/**
 * Error context for providing additional information about errors
 */
data class ErrorContext(
    val toolId: String? = null,
    val stepId: String? = null,
    val fieldId: String? = null,
    val retryCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * Enhanced error result with context
 */
sealed class AppErrorResult {
    data class Success(val data: Any? = null) : AppErrorResult()
    data class Failure(
        val kind: AppErrorKind,
        val message: String,
        val context: ErrorContext = ErrorContext(),
        val cause: Throwable? = null,
    ) : AppErrorResult()
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
    AppErrorKind.GenerationFailed -> R.string.generation_failed_retry
    AppErrorKind.GenerationCancelled -> R.string.generation_failed_retry
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
    AppErrorKind.SubscriptionExpired -> R.string.subscription_expired_error
    else -> fallback
}

@StringRes
fun AppErrorKind.purchaseAttemptMessageRes(fallback: Int): Int = when (this) {
    AppErrorKind.Offline -> R.string.purchase_offline_retry
    AppErrorKind.Timeout -> R.string.purchase_timeout_retry
    AppErrorKind.PaymentFailed -> R.string.purchase_offline_retry
    AppErrorKind.SubscriptionExpired -> R.string.subscription_expired_error
    else -> fallback
}

@StringRes
fun AppErrorKind.storeMessageRes(fallback: Int): Int = when (this) {
    AppErrorKind.Offline -> R.string.store_offline_retry
    AppErrorKind.Timeout -> R.string.store_timeout_retry
    AppErrorKind.PaymentFailed -> R.string.store_offline_retry
    else -> fallback
}

/**
 * Get user-friendly message for validation errors
 */
@StringRes
fun AppErrorKind.validationMessageRes(): Int = when (this) {
    AppErrorKind.ValidationError -> R.string.validation_error_generic
    AppErrorKind.RequiredFieldMissing -> R.string.required_field_missing
    AppErrorKind.InvalidInput -> R.string.invalid_input_error
    AppErrorKind.Mask -> R.string.mark_area_before_generate
    else -> R.string.validation_error_generic
}

/**
 * Check if this error is recoverable (user can retry)
 */
fun AppErrorKind.isRecoverable(): Boolean = when (this) {
    AppErrorKind.Offline,
    AppErrorKind.Timeout,
    AppErrorKind.Limit,
    AppErrorKind.Mask,
    AppErrorKind.ImagePreparation,
    AppErrorKind.Generation,
    AppErrorKind.GenerationFailed,
    AppErrorKind.ValidationError,
    AppErrorKind.RequiredFieldMissing,
    AppErrorKind.InvalidInput -> true
    else -> false
}

/**
 * Get the appropriate icon for this error type
 */
@StringRes
fun AppErrorKind.iconRes(): Int = when (this) {
    AppErrorKind.Offline,
    AppErrorKind.Timeout -> R.drawable.ic_refresh
    AppErrorKind.Limit -> R.drawable.ic_diamond
    AppErrorKind.Mask -> R.drawable.ic_brush
    AppErrorKind.ImagePreparation -> R.drawable.ic_photo
    AppErrorKind.Generation,
    AppErrorKind.GenerationFailed,
    AppErrorKind.GenerationCancelled -> R.drawable.ic_auto_awesome
    AppErrorKind.ValidationError,
    AppErrorKind.RequiredFieldMissing,
    AppErrorKind.InvalidInput -> R.drawable.ic_warning
    AppErrorKind.PurchaseSync,
    AppErrorKind.Store,
    AppErrorKind.PaymentFailed,
    AppErrorKind.SubscriptionExpired -> R.drawable.ic_store
    else -> R.drawable.ic_error
}
