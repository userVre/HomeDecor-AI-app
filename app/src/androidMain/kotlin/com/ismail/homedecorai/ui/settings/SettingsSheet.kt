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
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Language
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
import com.ismail.homedecorai.model.HomeDecorUiState
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
    onOpenDiamondStore: () -> Unit,
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
            .background(MaterialTheme.colorScheme.background)
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
                Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
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
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
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

                // ── Account ──
                if (signedIn) {
                    item {
                        SettingsSectionHeader(stringResource(R.string.settings_section_account))
                    }
                    item {
                        SettingsCardSurface {
                            SettingsRow(
                                Icons.Rounded.Person,
                                stringResource(R.string.edit_profile),
                                stringResource(R.string.settings_account_body),
                                iconTint = MaterialTheme.colorScheme.secondary,
                                onClick = {},
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Diamond,
                                stringResource(R.string.my_diamonds),
                                stringResource(R.string.my_diamonds_body, state.diamonds),
                                iconTint = HomeDecorExtra.diamondAccent,
                                onClick = {
                                    onClose()
                                    onOpenDiamondStore()
                                },
                            )
                        }
                    }
                }

                // ── App ──
                item {
                    SettingsSectionHeader(stringResource(R.string.settings_section_app))
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.Rounded.Language,
                            stringResource(R.string.language),
                            AppLocale.labelFor(context, currentLanguageTag),
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            onClick = { languagePickerVisible = true },
                        )
                    }
                }

                // ── Purchases ──
                item {
                    SettingsSectionHeader(stringResource(R.string.settings_section_purchases))
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.Rounded.Diamond,
                            stringResource(R.string.diamond_store),
                            stringResource(R.string.settings_diamond_store_body),
                            iconTint = HomeDecorExtra.diamondAccent,
                            onClick = {
                                onClose()
                                onOpenDiamondStore()
                            },
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Refresh,
                            stringResource(R.string.restore_purchases),
                            when {
                                restoring -> stringResource(R.string.restoring)
                                !Purchases.isConfigured -> stringResource(R.string.restore_unavailable)
                                else -> stringResource(R.string.settings_restore_body)
                            },
                            iconTint = MaterialTheme.colorScheme.primary,
                            onClick = { restorePurchases() },
                            trailingLoading = restoring,
                        )
                    }
                }

                // ── Support ──
                item {
                    SettingsSectionHeader(stringResource(R.string.settings_section_support))
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.AutoMirrored.Rounded.Help,
                            stringResource(R.string.faq),
                            stringResource(R.string.settings_faq_body),
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/faq"))) },
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Share,
                            stringResource(R.string.share_app),
                            stringResource(R.string.settings_share_body),
                            iconTint = MaterialTheme.colorScheme.secondary,
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
                            Icons.Rounded.RateReview,
                            stringResource(R.string.rate_us),
                            stringResource(R.string.settings_rate_body),
                            iconTint = MaterialTheme.colorScheme.primary,
                            onClick = { openGooglePlayReview(context) },
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Feedback,
                            stringResource(R.string.feedback),
                            stringResource(R.string.settings_feedback_body),
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = {
                                if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else feedbackDialogVisible = true
                            },
                        )
                    }
                }

                // ── Legal ──
                item {
                    SettingsSectionHeader(stringResource(R.string.settings_section_legal))
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.Rounded.Policy,
                            stringResource(R.string.terms),
                            stringResource(R.string.settings_terms_body),
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/terms"))) },
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Policy,
                            stringResource(R.string.privacy_policy),
                            stringResource(R.string.settings_privacy_body),
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = { setLinkFailureMessage(openUrlSafely(context, appUrl("/privacy"))) },
                        )
                    }
                }

                // ── Account actions (signed in only) ──
                if (signedIn) {
                    item {
                        SettingsSectionHeader(stringResource(R.string.account_section))
                    }
                    item {
                        SettingsCardSurface {
                            SettingsRow(
                                Icons.AutoMirrored.Rounded.Logout,
                                stringResource(R.string.log_out),
                                state.signedInEmail ?: stringResource(R.string.settings_logout_body),
                                iconTint = MaterialTheme.colorScheme.error,
                                onClick = {
                                    if (actionBusy) {
                                        settingsMessage = resources.getString(R.string.settings_action_in_progress)
                                    } else {
                                        logoutDialogVisible = true
                                    }
                                },
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Delete,
                                stringResource(R.string.delete_account),
                                stringResource(R.string.settings_delete_body),
                                iconTint = MaterialTheme.colorScheme.error,
                                onClick = {
                                    if (actionBusy) settingsMessage = resources.getString(R.string.settings_action_in_progress) else deleteDialogVisible = true
                                },
                            )
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
private fun SettingsSectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Xs),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SettingsCardSurface(
    content: @Composable () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(vertical = 6.dp)) {
            content()
        }
    }
}
