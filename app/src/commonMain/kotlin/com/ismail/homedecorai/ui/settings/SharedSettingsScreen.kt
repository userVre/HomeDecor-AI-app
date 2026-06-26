package com.ismail.homedecorai.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.showToast
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import kotlinx.coroutines.launch

data class SettingsLanguage(
    val tag: String,
    val label: String,
)

data class SettingsScreenState(
    val versionName: String = "1.0.0",
    val settingsBusy: Boolean = false,
    val isSignedIn: Boolean = false,
    val signedInName: String? = null,
    val signedInEmail: String? = null,
    val diamonds: Int = 0,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedSettingsScreen(
    state: SettingsScreenState,
    currentLanguageTag: String,
    supportedLanguages: List<SettingsLanguage>,
    onLanguageSelected: (String) -> Unit,
    onRateUs: () -> Unit,
    onContactSupport: () -> Unit,
    onDeleteInformation: () -> Unit,
    onSubmitFeedback: (String) -> Unit,
    onConfirmDelete: () -> Unit,
    onEditProfile: () -> Unit = {},
    onOpenDiamonds: () -> Unit = {},
    onOpenPaywall: () -> Unit = {},
    onFaq: () -> Unit = {},
    onShareApp: () -> Unit = {},
    onTerms: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLogout: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    val isDesktop = rememberIsDesktop()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var languageSheetVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }
    var logoutDialogVisible by remember { mutableStateOf(false) }

    if (languageSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { languageSheetVisible = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            LanguagePickerContent(
                currentLanguageTag = currentLanguageTag,
                supportedLanguages = supportedLanguages,
                onLanguageSelected = { languageTag ->
                    onLanguageSelected(languageTag)
                    showToast(Strings.toastLanguageSelected)
                    scope.launch {
                        sheetState.hide()
                        languageSheetVisible = false
                    }
                },
            )
        }
    } else if (isDesktop) {
        DesktopSettingsPage(
            state = state,
            currentLanguageTag = currentLanguageTag,
            onEditProfile = onEditProfile,
            onOpenDiamonds = onOpenDiamonds,
            onLanguageClick = { languageSheetVisible = true },
            onFaq = onFaq,
            onShareApp = onShareApp,
            onRateUs = onRateUs,
            onFeedback = { feedbackDialogVisible = true },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { logoutDialogVisible = true },
            onDeleteAccount = { deleteDialogVisible = true },
            onClose = onClose,
        )
    } else {
        MobileSettingsScreen(
            state = state,
            currentLanguageTag = currentLanguageTag,
            onEditProfile = onEditProfile,
            onOpenDiamonds = onOpenDiamonds,
            onLanguageClick = { languageSheetVisible = true },
            onFaq = onFaq,
            onShareApp = onShareApp,
            onRateUs = onRateUs,
            onFeedback = { feedbackDialogVisible = true },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { logoutDialogVisible = true },
            onDeleteAccount = { deleteDialogVisible = true },
        )
    }

    if (feedbackDialogVisible) {
        FeedbackDialog(
            busy = state.settingsBusy,
            onSubmit = { message ->
                onSubmitFeedback(message)
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
                onConfirmDelete()
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
}

// ── Desktop Full-Page Settings ──────────────────────────────────────────────

@Composable
private fun DesktopSettingsPage(
    state: SettingsScreenState,
    currentLanguageTag: String,
    onEditProfile: () -> Unit,
    onOpenDiamonds: () -> Unit,
    onLanguageClick: () -> Unit,
    onFaq: () -> Unit,
    onShareApp: () -> Unit,
    onRateUs: () -> Unit,
    onFeedback: () -> Unit,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 80.dp)
                .padding(top = 40.dp),
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        Strings.settings,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        Strings.settingsAppDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = Strings.close,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Content: 2-column grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                // Left column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    if (state.isSignedIn) {
                        DesktopSettingsSection(
                            title = Strings.settingsSectionAccount,
                            description = Strings.settingsAccountDescription,
                        ) {
                            SettingsRow(
                                Icons.Rounded.Person,
                                Strings.editProfile,
                                Strings.editProfileBody,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                onClick = onEditProfile,
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Diamond,
                                Strings.myDiamonds,
                                Strings.myDiamondsBody(state.diamonds),
                                iconTint = HomeDecorExtra.diamondAccent,
                                onClick = onOpenDiamonds,
                            )
                        }
                    }

                    DesktopSettingsSection(
                        title = Strings.settingsSectionApp,
                        description = Strings.settingsAppDescription,
                    ) {
                        SettingsRow(
                            Icons.Rounded.Language,
                            Strings.language,
                            currentLanguageTag.uppercase(),
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            onClick = onLanguageClick,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Star,
                            Strings.settingsAppearance,
                            Strings.settingsAppearanceBody,
                            iconTint = MaterialTheme.colorScheme.primary,
                            onClick = {},
                        )
                    }

                    DesktopSettingsSection(
                        title = Strings.settingsSectionLegal,
                        description = Strings.settingsLegalDescription,
                    ) {
                        SettingsRow(
                            Icons.Rounded.Policy,
                            Strings.terms,
                            Strings.termsBody,
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = onTerms,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Policy,
                            Strings.privacyPolicy,
                            Strings.privacyBody,
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = onPrivacy,
                        )
                    }
                }

                // Right column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    DesktopSettingsSection(
                        title = Strings.settingsSectionPurchases,
                        description = Strings.settingsPurchasesDescription,
                    ) {
                        SettingsRow(
                            Icons.Rounded.Diamond,
                            Strings.diamondStore,
                            Strings.diamondStoreBody,
                            iconTint = HomeDecorExtra.diamondAccent,
                            onClick = onOpenDiamonds,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Refresh,
                            Strings.restorePurchases,
                            Strings.restorePurchasesBody,
                            iconTint = MaterialTheme.colorScheme.primary,
                            onClick = {},
                        )
                    }

                    DesktopSettingsSection(
                        title = Strings.settingsSectionSupport,
                        description = Strings.settingsSupportDescription,
                    ) {
                        SettingsRow(
                            Icons.AutoMirrored.Rounded.Help,
                            Strings.faq,
                            Strings.faqBody,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            onClick = onFaq,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Share,
                            Strings.shareApp,
                            Strings.shareAppBody,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            onClick = onShareApp,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Star,
                            Strings.rateUs,
                            Strings.rateUsBody,
                            iconTint = MaterialTheme.colorScheme.primary,
                            onClick = onRateUs,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Feedback,
                            Strings.contactSupport,
                            Strings.feedbackBody,
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            onClick = onFeedback,
                        )
                    }

                    if (state.isSignedIn) {
                        DesktopSettingsSection(
                            title = Strings.settingsSectionAccount,
                            description = Strings.settingsAccountDescription,
                        ) {
                            SettingsRow(
                                Icons.AutoMirrored.Rounded.Logout,
                                Strings.logOut,
                                state.signedInEmail ?: Strings.logOutBody,
                                iconTint = MaterialTheme.colorScheme.error,
                                onClick = onLogout,
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Delete,
                                Strings.deleteAccountTitle,
                                Strings.deleteAccountBody,
                                iconTint = MaterialTheme.colorScheme.error,
                                onClick = onDeleteAccount,
                            )
                        }
                    }
                }
            }

            // Version at bottom
            Text(
                Strings.versionLabel(state.versionName),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun DesktopSettingsSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(24.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

// ── Mobile Full-Screen ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileSettingsScreen(
    state: SettingsScreenState,
    currentLanguageTag: String,
    onEditProfile: () -> Unit,
    onOpenDiamonds: () -> Unit,
    onLanguageClick: () -> Unit,
    onFaq: () -> Unit,
    onShareApp: () -> Unit,
    onRateUs: () -> Unit,
    onFeedback: () -> Unit,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        Strings.settings,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (state.isSignedIn) {
                item {
                    SettingsSectionHeader(Strings.settingsSectionAccount)
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.Rounded.Person,
                            Strings.editProfile,
                            Strings.editProfileBody,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            onClick = onEditProfile,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Diamond,
                            Strings.myDiamonds,
                            Strings.myDiamondsBody(state.diamonds),
                            iconTint = HomeDecorExtra.diamondAccent,
                            onClick = onOpenDiamonds,
                        )
                    }
                }
            }

            item {
                SettingsSectionHeader(Strings.settingsSectionApp)
            }
            item {
                SettingsCardSurface {
                    SettingsRow(
                        Icons.Rounded.Language,
                        Strings.language,
                        currentLanguageTag.uppercase(),
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        onClick = onLanguageClick,
                    )
                }
            }

            item {
                SettingsSectionHeader(Strings.settingsSectionPurchases)
            }
            item {
                SettingsCardSurface {
                    SettingsRow(
                        Icons.Rounded.Diamond,
                        Strings.diamondStore,
                        Strings.diamondStoreBody,
                        iconTint = HomeDecorExtra.diamondAccent,
                        onClick = onOpenDiamonds,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Refresh,
                        Strings.restorePurchases,
                        Strings.restorePurchasesBody,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = {},
                    )
                }
            }

            item {
                SettingsSectionHeader(Strings.settingsSectionSupport)
            }
            item {
                SettingsCardSurface {
                    SettingsRow(
                        Icons.AutoMirrored.Rounded.Help,
                        Strings.faq,
                        Strings.faqBody,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        onClick = onFaq,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Share,
                        Strings.shareApp,
                        Strings.shareAppBody,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        onClick = onShareApp,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Star,
                        Strings.rateUs,
                        Strings.rateUsBody,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = onRateUs,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Feedback,
                        Strings.contactSupport,
                        Strings.feedbackBody,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onFeedback,
                    )
                }
            }

            item {
                SettingsSectionHeader(Strings.settingsSectionLegal)
            }
            item {
                SettingsCardSurface {
                    SettingsRow(
                        Icons.Rounded.Policy,
                        Strings.terms,
                        Strings.termsBody,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onTerms,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Policy,
                        Strings.privacyPolicy,
                        Strings.privacyBody,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onPrivacy,
                    )
                }
            }

            if (state.isSignedIn) {
                item {
                    SettingsSectionHeader(Strings.settingsSectionAccount)
                }
                item {
                    SettingsCardSurface {
                        SettingsRow(
                            Icons.AutoMirrored.Rounded.Logout,
                            Strings.logOut,
                            state.signedInEmail ?: Strings.logOutBody,
                            iconTint = MaterialTheme.colorScheme.error,
                            onClick = onLogout,
                        )
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Delete,
                            Strings.deleteAccountTitle,
                            Strings.deleteAccountBody,
                            iconTint = MaterialTheme.colorScheme.error,
                            onClick = onDeleteAccount,
                        )
                    }
                }
            }

            item {
                Text(
                    Strings.versionLabel(state.versionName),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 32.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
        }
    }
}

// ── Shared Composables ───────────────────────────────────────────────────────

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
private fun SettingsCardSurface(content: @Composable () -> Unit) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(vertical = 6.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    trailingLoading: Boolean = false,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                title,
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
            Surface(
                shape = CircleShape,
                color = if (enabled) iconTint.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
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
                CircularProgressIndicator(
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
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    )
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f))
}

// ── Language Picker ──────────────────────────────────────────────────────────

@Composable
private fun LanguagePickerContent(
    currentLanguageTag: String,
    supportedLanguages: List<SettingsLanguage>,
    onLanguageSelected: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            Text(
                Strings.language,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )
        }
        item {
            val systemSelected = currentLanguageTag == "system"
            LanguageRow(
                label = Strings.languageSystemDefault,
                selected = systemSelected,
                onClick = { onLanguageSelected("system") },
            )
            SettingsDivider()
        }
        items(supportedLanguages.size) { index ->
            val language = supportedLanguages[index]
            val selected = language.tag == currentLanguageTag
            LanguageRow(
                label = language.label,
                selected = selected,
                onClick = { onLanguageSelected(language.tag) },
            )
            SettingsDivider()
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
private fun LanguageRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val languageDescription = if (selected) {
        Strings.a11yLanguageSelected(label)
    } else {
        label
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                this.selected = selected
                contentDescription = languageDescription
                role = Role.Button
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = Strings.a11yCheckIcon,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

// ── Dialogs ──────────────────────────────────────────────────────────────────

@Composable
fun FeedbackDialog(
    busy: Boolean,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.feedbackDialogTitle) },
        text = {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                placeholder = { Text(Strings.feedbackHint) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onSubmit(message) },
                enabled = message.isNotBlank() && !busy,
            ) {
                Text(if (busy) Strings.feedbackSending else Strings.feedbackSend)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.cancel)
            }
        },
    )
}

@Composable
fun DeleteAccountDialog(
    busy: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.deleteAccountTitle) },
        text = { Text(Strings.deleteAccountBody) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !busy,
            ) {
                Text(
                    if (busy) Strings.deletingAccount else Strings.deleteAccountConfirm,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.cancel)
            }
        },
    )
}

@Composable
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(Strings.logOut) },
        text = { Text(Strings.logOutBody) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    Strings.logOut,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Strings.cancel)
            }
        },
    )
}
