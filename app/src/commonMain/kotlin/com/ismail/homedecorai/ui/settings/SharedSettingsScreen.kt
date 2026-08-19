package com.ismail.homedecorai.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ToastState
import com.ismail.homedecorai.model.SettingsDialog
import com.ismail.homedecorai.model.SettingsLanguage
import com.ismail.homedecorai.model.SettingsScreenState
import com.ismail.homedecorai.ui.components.ResponsiveDialog
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled

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
    isDesktop: Boolean = false,
) {
    var activeDialog by remember { mutableStateOf<SettingsDialog>(SettingsDialog.None) }
    var confirmLogout by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var showLanguage by remember { mutableStateOf(false) }

    if (showLanguage) {
        LanguagePickerDialog(
            currentLanguageTag = currentLanguageTag,
            supportedLanguages = supportedLanguages,
            onLanguageSelected = { tag ->
                onLanguageSelected(tag)
                ToastState.show(Strings.toastLanguageSelected)
                showLanguage = false
            },
            onDismiss = { showLanguage = false },
        )
    }

    if (showFeedback) {
        FeedbackDialog(
            busy = state.settingsBusy,
            onSubmit = { message ->
                onSubmitFeedback(message)
                showFeedback = false
            },
            onDismiss = { showFeedback = false },
        )
    }

    if (confirmLogout) {
        ConfirmActionDialog(
            title = Strings.logOut,
            body = Strings.logOutBody,
            confirmLabel = Strings.logOut,
            onConfirm = {
                confirmLogout = false
                onLogout()
            },
            onDismiss = { confirmLogout = false },
        )
    }

    if (confirmDelete) {
        ConfirmActionDialog(
            title = Strings.deleteAccountTitle,
            body = Strings.deleteAccountBody,
            confirmLabel = Strings.deleteAccountConfirm,
            onConfirm = {
                confirmDelete = false
                onConfirmDelete()
            },
            onDismiss = { confirmDelete = false },
        )
    }

    if (isDesktop) {
        DesktopSettingsPage(
            state = state,
            currentLanguageTag = currentLanguageTag,
            onEditProfile = onEditProfile,
            onOpenDiamonds = onOpenDiamonds,
            onLanguageClick = { showLanguage = true },
            onFaq = onFaq,
            onContactSupport = onContactSupport,
            onFeedback = { showFeedback = true },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { confirmLogout = true },
            onDeleteAccount = { confirmDelete = true },
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
            onLanguageClick = { showLanguage = true },
            onFaq = onFaq,
            onContactSupport = onContactSupport,
            onFeedback = { showFeedback = true },
            onTerms = onTerms,
            onPrivacy = onPrivacy,
            onLogout = { confirmLogout = true },
            onDeleteAccount = { confirmDelete = true },
            onManageBilling = onManageBilling,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle,
            onClose = onClose,
        )
    }
}

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
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
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
                        contentDescription = Strings.a11ySettingsClose,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = HomeDecorSpacing.DesktopContentHorizontalPadding,
                end = HomeDecorSpacing.DesktopContentHorizontalPadding,
                top = HomeDecorSpacing.Xl,
                bottom = HomeDecorSpacing.Xxl,
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().widthIn(max = 600.dp),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Lg),
                ) {
                    if (state.isSignedIn) {
                        DesktopSettingsSection(title = Strings.settingsSectionAccount) {
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

                    DesktopSettingsSection(title = Strings.settingsSectionApp) {
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

                    DesktopSettingsSection(title = Strings.settingsSectionPurchases) {
                        SettingsRow(
                            Icons.Rounded.Diamond,
                            Strings.diamondStore,
                            Strings.diamondStoreBody,
                            iconTint = HomeDecorExtra.diamondAccent,
                            onClick = onOpenDiamonds,
                        )
                    }

                    DesktopSettingsSection(title = Strings.settingsSectionSupport) {
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

                    DesktopSettingsSection(title = Strings.settingsSectionLegal) {
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

                    if (state.isSignedIn) {
                        DesktopSettingsSection(title = Strings.settingsSectionAccountActions) {
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

                    Text(
                        Strings.versionLabel(state.versionName),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
}

@Composable
private fun DesktopSettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.Level1,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(horizontal = HomeDecorSpacing.Lg, vertical = HomeDecorSpacing.Base)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(HomeDecorSpacing.Sm))
            content()
        }
    }
}

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
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onClose: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            if (state.isSignedIn) {
                item { SettingsSectionHeader(Strings.settingsSectionAccount) }
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

            item { SettingsSectionHeader(Strings.settingsSectionApp) }
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

            item { SettingsSectionHeader(Strings.settingsSectionPurchases) }
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

            item { SettingsSectionHeader(Strings.settingsSectionSupport) }
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

            item { SettingsSectionHeader(Strings.settingsSectionLegal) }
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
                item { SettingsSectionHeader(Strings.settingsSectionAccountActions) }
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

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        title,
        modifier = Modifier
            .padding(horizontal = HomeDecorSpacing.Base, vertical = HomeDecorSpacing.Sm)
            .semantics { heading() },
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
internal fun SettingsCardSurface(content: @Composable () -> Unit) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HomeDecorElevation.Level1,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base),
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "settingsRowScale",
    )

    ListItem(
        headlineContent = {
            Text(
                title,
                color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurface,
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
                    modifier = Modifier.padding(9.dp).size(HomeDecorIconSize.Medium),
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
                    modifier = Modifier.size(HomeDecorIconSize.Medium),
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = when {
                !enabled -> MaterialTheme.colorScheme.surfaceContainerHigh
                isHovered -> MaterialTheme.colorScheme.surfaceContainerLow
                else -> MaterialTheme.colorScheme.surface
            },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .testTag(Strings.formatTestTag(Strings.TestTags.settingsRow, title.lowercase()))
            .semantics {
                contentDescription = "$title. $subtitle"
                role = Role.Button
            }
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    )
}

@Composable
internal fun SettingsDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}

// ── Language Picker ──────────────────────────────────────────────────────────

@Composable
fun LanguagePickerDialog(
    currentLanguageTag: String,
    supportedLanguages: List<SettingsLanguage>,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = Strings.language,
        subtitle = Strings.languageChangeImmediate,
        maxWidth = 400.dp,
    ) {
        val systemSelected = currentLanguageTag == "system"
        LanguageRow(
            label = Strings.languageSystemDefault,
            selected = systemSelected,
            onClick = { onLanguageSelected("system") },
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        supportedLanguages.forEach { language ->
            val selected = language.tag == currentLanguageTag
            LanguageRow(
                label = language.label,
                selected = selected,
                onClick = { onLanguageSelected(language.tag) },
            )
            if (language != supportedLanguages.last()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
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
            .padding(vertical = HomeDecorSpacing.Base),
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

    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = Strings.feedbackDialogTitle,
        maxWidth = 400.dp,
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(Strings.cancel)
                }
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                TextButton(
                    onClick = { onSubmit(message) },
                    enabled = message.isNotBlank() && !busy,
                ) {
                    Text(if (busy) Strings.feedbackSending else Strings.feedbackSend)
                }
            }
        },
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text(Strings.feedbackHint) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
        )
    }
}

@Composable
private fun ConfirmActionDialog(
    title: String,
    body: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ResponsiveDialog(
        onDismissRequest = onDismiss,
        title = title,
        maxWidth = 400.dp,
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(Strings.cancel)
                }
                Spacer(Modifier.width(HomeDecorSpacing.Sm))
                TextButton(onClick = onConfirm) {
                    Text(
                        confirmLabel,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        },
    ) {
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
