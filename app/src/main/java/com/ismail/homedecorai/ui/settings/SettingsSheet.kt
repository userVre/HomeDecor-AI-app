package com.ismail.homedecorai.ui.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.R
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
import com.ismail.homedecorai.ui.components.*
import com.ismail.homedecorai.ui.dialogs.*
import com.ismail.homedecorai.ui.components.PurchaseSyncNotice
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.*
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback

@Composable
fun SettingsSheet(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onFeedback: (String) -> Unit,
    onDeleteAccount: () -> Unit,
    onLogout: () -> Unit,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val modalTapBlocker = remember { MutableInteractionSource() }
    var languagePickerVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var logoutDialogVisible by remember { mutableStateOf(false) }
    var unavailableMessage by remember { mutableStateOf<String?>(null) }
    var restoring by remember { mutableStateOf(false) }
    var settingsMessage by remember { mutableStateOf<String?>(null) }
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    val actionBusy = restoring || state.settingsBusy || state.purchaseBusy
    fun setLinkFailureMessage(opened: Boolean) {
        if (!opened) {
            settingsMessage = resources.getString(R.string.open_link_failed)
        }
    }
    fun showTemporaryUnavailable(message: String = resources.getString(R.string.settings_temporarily_unavailable_body)) {
        unavailableMessage = message
    }
    fun restorePurchases() {
        if (actionBusy) {
            settingsMessage = resources.getString(R.string.settings_action_in_progress)
            return
        }
        if (!Purchases.isConfigured) {
            showTemporaryUnavailable(resources.getString(R.string.restore_unavailable_body))
            return
        }
        restoring = true
        settingsMessage = null
        Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
            override fun onReceived(customerInfo: CustomerInfo) {
                restoring = false
                val active = customerInfo.entitlements.active.values.firstOrNull()
                if (active != null) {
                    settingsMessage = resources.getString(R.string.pro_syncing)
                    onSubscription(
                        "pro",
                        if (active.identifier.contains("annual")) "yearly" else "monthly",
                        active.identifier,
                        active.latestPurchaseDate.time.toDouble(),
                        active.expirationDate?.time?.toDouble(),
                    )
                } else {
                    settingsMessage = resources.getString(R.string.no_active_pro_purchase)
                }
            }

            override fun onError(error: PurchasesError) {
                restoring = false
                settingsMessage = resources.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
            }
        })
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .clickable(
                interactionSource = modalTapBlocker,
                indication = null,
                onClick = {},
            ),
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                }
                Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
            }
            LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val actionMessage = (settingsMessage ?: state.settingsMessage ?: state.purchaseMessage)?.takeIf { it.isNotBlank() }
                if (actionMessage != null) {
                    item {
                        if (state.pendingPurchaseSync != null && settingsMessage == null && state.settingsMessage == null) {
                            PurchaseSyncNotice(
                                message = actionMessage,
                                pending = true,
                                busy = state.purchaseBusy,
                                onRetry = onRetrySync,
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = StudioMist,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    actionMessage,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = StudioPaper,
                        tonalElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(vertical = 6.dp)) {
                            SettingsRow(
                                Icons.Rounded.Language,
                                stringResource(R.string.language),
                                AppLocale.labelFor(context, currentLanguageTag),
                                onClick = { languagePickerVisible = true },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Star,
                                stringResource(R.string.rate_us),
                                stringResource(R.string.rate_us_subtitle),
                                onClick = { openGooglePlayReview(context) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Share,
                                stringResource(R.string.share_app),
                                stringResource(R.string.share_app_subtitle),
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${context.packageName}")
                                    }
                                    runCatching {
                                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_app_chooser)))
                                    }
                                },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.AutoMirrored.Rounded.Help,
                                stringResource(R.string.faq),
                                stringResource(R.string.faq_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/faq"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Policy,
                                stringResource(R.string.terms),
                                stringResource(R.string.terms_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/terms"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Lock,
                                stringResource(R.string.privacy_policy),
                                stringResource(R.string.privacy_subtitle),
                                onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/privacy"))) },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.RateReview,
                                stringResource(R.string.feedback),
                                stringResource(R.string.feedback_subtitle),
                                onClick = {
                                    if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else feedbackDialogVisible = true
                                },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Refresh,
                                stringResource(R.string.restore_purchases),
                                when {
                                    restoring -> stringResource(R.string.restoring)
                                    !Purchases.isConfigured -> stringResource(R.string.restore_unavailable)
                                    else -> stringResource(R.string.restore_purchases_subtitle)
                                },
                                onClick = { restorePurchases() },
                                trailingLoading = restoring,
                            )
                        }
                    }
                }
                item {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = StudioPaper,
                        tonalElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioLine),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(Modifier.padding(vertical = 6.dp)) {
                            SettingsRow(
                                Icons.Rounded.Delete,
                                stringResource(R.string.delete_account),
                                stringResource(R.string.delete_account_subtitle),
                                iconTint = StudioRose,
                                onClick = {
                                    if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else deleteDialogVisible = true
                                },
                            )
                            if (signedIn) {
                                SettingsDivider()
                                SettingsRow(
                                    Icons.AutoMirrored.Rounded.Logout,
                                    stringResource(R.string.log_out),
                                    state.signedInEmail ?: stringResource(R.string.logout_subtitle),
                                    iconTint = StudioRose,
                                    onClick = {
                                        if (actionBusy) {
                                            settingsMessage = resources.getString(R.string.settings_action_in_progress)
                                        } else {
                                            logoutDialogVisible = true
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
                item {
                    Text(
                        stringResource(R.string.version_label, "1.0.0"),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
    if (languagePickerVisible) {
        LanguagePickerDialog(
            currentLanguageTag = currentLanguageTag,
            onLanguageSelected = { languageTag ->
                onLanguageSelected(languageTag)
                val localizedContext = AppLocale.wrap(context, languageTag)
                Toast.makeText(
                    localizedContext,
                    localizedContext.getString(R.string.toast_language_selected),
                    Toast.LENGTH_LONG,
                ).show()
                languagePickerVisible = false
            },
            onDismiss = { languagePickerVisible = false },
        )
    }
    if (feedbackDialogVisible) {
        FeedbackDialog(
            busy = state.settingsBusy,
            onSubmit = { message ->
                onFeedback(message)
                feedbackDialogVisible = false
            },
            onDismiss = { feedbackDialogVisible = false },
        )
    }
    if (deleteDialogVisible) {
        DeleteAccountDialog(
            busy = state.settingsBusy,
            onConfirm = {
                deleteDialogVisible = false
                onDeleteAccount()
            },
            onDismiss = { deleteDialogVisible = false },
        )
    }
    if (logoutDialogVisible) {
        LogoutDialog(
            onConfirm = {
                logoutDialogVisible = false
                onLogout()
            },
            onDismiss = { logoutDialogVisible = false },
        )
    }
    unavailableMessage?.let { message ->
        TemporaryUnavailableDialog(
            message = message,
            onDismiss = { unavailableMessage = null },
        )
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    iconTint: Color = StudioBlue,
    trailingLoading: Boolean = false,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        supportingContent = {
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            Surface(shape = CircleShape, color = if (enabled) iconTint.copy(alpha = 0.12f) else StudioMist) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (enabled) iconTint else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(9.dp).size(20.dp),
                )
            }
        },
        trailingContent = {
            if (trailingLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp),
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = if (enabled) StudioPaper else StudioMist,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .clip(RoundedCornerShape(18.dp))
            .disabledSemantics(enabled)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    )
}

