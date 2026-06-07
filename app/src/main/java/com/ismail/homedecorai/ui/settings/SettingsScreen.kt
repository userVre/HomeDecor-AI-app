package com.ismail.homedecorai.ui.settings

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.MainTab
import com.ismail.homedecorai.R
import com.ismail.homedecorai.rawServiceMessageToKind
import com.ismail.homedecorai.storeMessageRes
import com.ismail.homedecorai.ui.dialogs.DeleteAccountDialog
import com.ismail.homedecorai.ui.dialogs.FeedbackDialog
import com.ismail.homedecorai.ui.dialogs.LanguagePickerDialog
import com.ismail.homedecorai.ui.theme.StudioBlue
import com.ismail.homedecorai.ui.theme.StudioCanvas
import com.ismail.homedecorai.ui.theme.StudioLine
import com.ismail.homedecorai.ui.theme.StudioRose
import com.ismail.homedecorai.ui.utility.appUrl
import com.ismail.homedecorai.ui.utility.openGooglePlayReview
import com.ismail.homedecorai.ui.utility.openUrlSafely
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    var languagePickerVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var restoring by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.selectTab(MainTab.Profile) }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioCanvas),
            )
        },
        containerColor = StudioCanvas,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                SettingsItemRow(
                    icon = Icons.Rounded.Language,
                    title = stringResource(R.string.language),
                    onClick = { languagePickerVisible = true },
                )
                SettingsFullDivider()
                SettingsItemRow(
                    icon = Icons.Rounded.Star,
                    title = stringResource(R.string.rate_us),
                    onClick = { openGooglePlayReview(context) },
                )
                SettingsFullDivider()
                SettingsItemRow(
                    icon = Icons.Rounded.Share,
                    title = stringResource(R.string.share_app),
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
                SettingsFullDivider()
                SettingsItemRow(
                    icon = Icons.AutoMirrored.Rounded.Help,
                    title = stringResource(R.string.faq),
                    onClick = { openUrlSafely(context, appUrl("/faq")) },
                )
                SettingsFullDivider()
                SettingsItemRow(
                    icon = Icons.Rounded.Policy,
                    title = stringResource(R.string.contact_support),
                    onClick = { feedbackDialogVisible = true },
                )
                SettingsFullDivider()
                SettingsItemRow(
                    icon = Icons.Rounded.Refresh,
                    title = stringResource(R.string.restore_purchases),
                    onClick = {
                        if (restoring) return@SettingsItemRow
                        if (!Purchases.isConfigured) {
                            statusMessage = "Purchases not available"
                            return@SettingsItemRow
                        }
                        restoring = true
                        statusMessage = null
                        Purchases.sharedInstance.restorePurchases(object : ReceiveCustomerInfoCallback {
                            override fun onReceived(customerInfo: CustomerInfo) {
                                restoring = false
                                val active = customerInfo.entitlements.active.values.firstOrNull()
                                statusMessage = if (active != null) {
                                    context.getString(R.string.pro_syncing)
                                } else {
                                    context.getString(R.string.no_active_pro_purchase)
                                }
                            }

                            override fun onError(error: PurchasesError) {
                                restoring = false
                                statusMessage = context.getString(rawServiceMessageToKind(context, error.message).storeMessageRes(R.string.restore_failed))
                            }
                        })
                    },
                )
            }

            SettingsFullDivider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                SettingsItemRow(
                    icon = Icons.Rounded.Delete,
                    title = stringResource(R.string.delete_account),
                    titleColor = StudioRose,
                    iconTint = StudioRose,
                    onClick = { deleteDialogVisible = true },
                )
            }

            statusMessage?.let { message ->
                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                stringResource(R.string.version_label, "1.0.0"),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
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
                viewModel.submitSettingsFeedback(message)
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
                viewModel.deleteAccountData()
            },
            onDismiss = { deleteDialogVisible = false },
        )
    }
}

@Composable
private fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    titleColor: Color = Color.Unspecified,
    iconTint: Color = StudioBlue,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { role = Role.Button }
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(shape = CircleShape, color = iconTint.copy(alpha = 0.12f)) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .padding(10.dp)
                    .size(22.dp),
            )
        }
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SettingsFullDivider() {
    HorizontalDivider(color = StudioLine.copy(alpha = 0.65f))
}
