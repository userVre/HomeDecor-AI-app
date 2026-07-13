package com.ismail.homedecorai.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Shield
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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ToastState
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*
import kotlinx.coroutines.launch

data class SettingsLanguage(
    val tag: String,
    val label: String,
)

private sealed class SettingsDialog {
    data object None : SettingsDialog()
    data object Language : SettingsDialog()
    data object Feedback : SettingsDialog()
    data object DeleteAccount : SettingsDialog()
    data object Logout : SettingsDialog()
}

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
    onTerms: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLogout: () -> Unit = {},
    onClose: () -> Unit = {},
    onManageBilling: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
) {
    val isDesktop = rememberIsDesktop()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var activeDialog by remember { mutableStateOf<SettingsDialog>(SettingsDialog.None) }

    if (activeDialog is SettingsDialog.Language) {
        if (isDesktop) {
            AlertDialog(
                onDismissRequest = { activeDialog = SettingsDialog.None },
            ) {
                Surface(
                    shape = HomeDecorShape.ExtraExtraLarge,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                ) {
                    LanguagePickerContent(
                        currentLanguageTag = currentLanguageTag,
                        supportedLanguages = supportedLanguages,
                        onLanguageSelected = { languageTag ->
                            onLanguageSelected(languageTag)
                            ToastState.show(Strings.toastLanguageSelected)
                            activeDialog = SettingsDialog.None
                        },
                    )
                }
            }
        } else {
            ModalBottomSheet(
                onDismissRequest = { activeDialog = SettingsDialog.None },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                LanguagePickerContent(
                    currentLanguageTag = currentLanguageTag,
                    supportedLanguages = supportedLanguages,
                    onLanguageSelected = { languageTag ->
                        onLanguageSelected(languageTag)
                        ToastState.show(Strings.toastLanguageSelected)
                        scope.launch {
                            sheetState.hide()
                            activeDialog = SettingsDialog.None
                        }
                    },
                )
            }
        }
    } else if (isDesktop) {
        DesktopSettingsPage(
            state = state,
            currentLanguageTag = currentLanguageTag,
            onEditProfile = onEditProfile,
            onOpenDiamonds = onOpenDiamonds,
            onLanguageClick = { activeDialog = SettingsDialog.Language },
            onFaq = onFaq,
            onContactSupport = onContactSupport,
            onFeedback = { activeDialog = SettingsDialog.Feedback },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { activeDialog = SettingsDialog.Logout },
            onDeleteAccount = { activeDialog = SettingsDialog.DeleteAccount },
            onClose = onClose,
            onManageBilling = onManageBilling,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle,
        )
    } else {
        MobileSettingsScreen(
            state = state,
            currentLanguageTag = currentLanguageTag,
            onEditProfile = onEditProfile,
            onOpenDiamonds = onOpenDiamonds,
            onLanguageClick = { activeDialog = SettingsDialog.Language },
            onFaq = onFaq,
            onContactSupport = onContactSupport,
            onFeedback = { activeDialog = SettingsDialog.Feedback },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { activeDialog = SettingsDialog.Logout },
            onDeleteAccount = { activeDialog = SettingsDialog.DeleteAccount },
            onManageBilling = onManageBilling,
            onClose = onClose,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle,
        )
    }

    when (activeDialog) {
        is SettingsDialog.Feedback -> FeedbackDialog(
            busy = state.settingsBusy,
            onSubmit = { message ->
                onSubmitFeedback(message)
                activeDialog = SettingsDialog.None
            },
            onDismiss = { activeDialog = SettingsDialog.None },
        )
        is SettingsDialog.DeleteAccount -> DeleteAccountDialog(
            busy = state.settingsBusy,
            onConfirm = {
                activeDialog = SettingsDialog.None
                onConfirmDelete()
            },
            onDismiss = { activeDialog = SettingsDialog.None },
        )
        is SettingsDialog.Logout -> LogoutDialog(
            onConfirm = {
                activeDialog = SettingsDialog.None
                onLogout()
            },
            onDismiss = { activeDialog = SettingsDialog.None },
        )
        else -> {}
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
    onContactSupport: () -> Unit,
    onFeedback: () -> Unit,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onClose: () -> Unit,
    onManageBilling: () -> Unit,
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Sticky header — never scrolls away
        Surface(
            color = MaterialTheme.colorScheme.background,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = HomeDecorSpacing.DesktopContentHorizontalPadding,
                        vertical = HomeDecorSpacing.Lg,
                    ),
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
        }

        // Scrollable body — legal, account actions, and all sections stay reachable
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.DesktopContentHorizontalPadding,
                end = HomeDecorSpacing.DesktopContentHorizontalPadding,
                top = HomeDecorSpacing.Xl,
                bottom = HomeDecorSpacing.Xxl,
            ),
        ) {
            // ── Row 1: Account (left) + Purchases & Support (right) ──
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().widthIn(max = 800.dp),
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
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
                                SettingsDivider()
                                SettingsRow(
                                    Icons.Rounded.Refresh,
                                    Strings.manageBilling,
                                    Strings.manageBillingBody,
                                    iconTint = MaterialTheme.colorScheme.tertiary,
                                    onClick = onManageBilling,
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
                                Icons.Rounded.DarkMode,
                                Strings.themeLabel,
                                if (isDarkTheme) Strings.themeDark else Strings.themeLight,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                onClick = onThemeToggle,
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
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
                        }

                        DesktopSettingsSection(
                            title = Strings.settingsSectionSupport,
                            description = Strings.settingsSupportDescription,
                        ) {
                            SettingsRow(
                                Icons.AutoMirrored.Rounded.Help,
                                Strings.helpCenter,
                                Strings.helpCenterBody,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                onClick = onFaq,
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Mail,
                                Strings.contactUs,
                                Strings.contactUsBody,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                onClick = onContactSupport,
                            )
                            SettingsDivider()
                            SettingsRow(
                                Icons.Rounded.Feedback,
                                Strings.sendFeedback,
                                Strings.feedbackBody,
                                iconTint = MaterialTheme.colorScheme.primary,
                                onClick = onFeedback,
                            )
                        }
                    }
                    }
                }
                Spacer(Modifier.height(HomeDecorSpacing.Lg))
            }

            // ── Row 2: Legal (left) + Account Actions (right, signed-in only) ──
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().widthIn(max = 800.dp),
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
                        ) {
                            DesktopSettingsSection(
                                title = Strings.settingsSectionLegal,
                                description = Strings.settingsLegalDescription,
                            ) {
                                SettingsRow(
                                    Icons.Rounded.Description,
                                    Strings.terms,
                                    Strings.termsBody,
                                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = onTerms,
                                )
                                SettingsDivider()
                                SettingsRow(
                                    Icons.Rounded.Shield,
                                    Strings.privacyPolicy,
                                    Strings.privacyBody,
                                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = onPrivacy,
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
                        ) {
                            if (state.isSignedIn) {
                                DesktopSettingsSection(
                                    title = Strings.settingsSectionAccountActions,
                                    description = Strings.settingsAccountActionsBody,
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
                }
            }

            // Version footer
            item {
                Text(
                    Strings.versionLabel(state.versionName),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
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
        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
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
    onContactSupport: () -> Unit,
    onFeedback: () -> Unit,
    onTerms: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onManageBilling: () -> Unit,
    onClose: () -> Unit,
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
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
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = Strings.close,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
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
                        SettingsDivider()
                        SettingsRow(
                            Icons.Rounded.Refresh,
                            Strings.manageBilling,
                            Strings.manageBillingBody,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            onClick = onManageBilling,
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
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.DarkMode,
                        Strings.themeLabel,
                        if (isDarkTheme) Strings.themeDark else Strings.themeLight,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        onClick = onThemeToggle,
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
                }
            }

            item {
                SettingsSectionHeader(Strings.settingsSectionSupport)
            }
            item {
                SettingsCardSurface {
                    SettingsRow(
                        Icons.AutoMirrored.Rounded.Help,
                        Strings.helpCenter,
                        Strings.helpCenterBody,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        onClick = onFaq,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Mail,
                        Strings.contactUs,
                        Strings.contactUsBody,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        onClick = onContactSupport,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Feedback,
                        Strings.sendFeedback,
                        Strings.feedbackBody,
                        iconTint = MaterialTheme.colorScheme.primary,
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
                        Icons.Rounded.Description,
                        Strings.terms,
                        Strings.termsBody,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onTerms,
                    )
                    SettingsDivider()
                    SettingsRow(
                        Icons.Rounded.Shield,
                        Strings.privacyPolicy,
                        Strings.privacyBody,
                        iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = onPrivacy,
                    )
                }
            }

            if (state.isSignedIn) {
                item {
                    SettingsSectionHeader(Strings.settingsSectionAccountActions)
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
                        .padding(horizontal = HomeDecorSpacing.Md)
                        .padding(top = HomeDecorSpacing.Xxl, bottom = HomeDecorSpacing.Xxl),
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
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Xs)
            .semantics { heading() },
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
        Column(Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
internal fun SettingsRow(
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
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
            .testTag(Strings.formatTestTag(Strings.TestTags.settingsRow, title.lowercase()))
            .semantics {
                contentDescription = "$title. $subtitle"
                role = Role.Button
            }
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    )
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

// ── Language Picker ──────────────────────────────────────────────────────────

@Composable
internal fun LanguagePickerContent(
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
