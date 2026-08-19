package com.ismail.homedecorai.ui.profile

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.model.BoardItem
import com.ismail.homedecorai.model.SettingsLanguage
import com.ismail.homedecorai.model.ProfileScreenState
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.showToast
import com.ismail.homedecorai.ui.rememberIsCompact
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.settings.LanguagePickerDialog
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.theme.isReducedMotionEnabled

@Composable
fun SharedProfileScreen(
    state: ProfileScreenState,
    onSettings: () -> Unit,
    onSignIn: () -> Unit,
    onOpenDiamonds: () -> Unit,
    onOpenPaywall: () -> Unit,
    onOpenBoard: () -> Unit = {},
    onNavigateToDiscover: () -> Unit = {},
    onOpenUrl: (String) -> Unit = {},
) {
    val signedIn = !state.isGuest && (state.signedInName != null || state.signedInEmail != null)
    val isDesktop = rememberIsDesktop()
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLanguageTag = "en",
            supportedLanguages = listOf(SettingsLanguage("en", "English")),
            onLanguageSelected = { showLanguageDialog = false },
            onDismiss = { showLanguageDialog = false },
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.profileScreen),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.ScreenHorizontal,
                end = if (isDesktop) HomeDecorSpacing.Xxl else HomeDecorSpacing.ScreenHorizontal,
                top = HomeDecorSpacing.Lg,
                bottom = navBarBottomPadding(additionalContentPadding = if (isDesktop) 0.dp else 24.dp),
            ),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.SectionGap),
        ) {
            item("profile-title") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(Strings.TestTags.profileHeading),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            Strings.myProfileTitle,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.semantics { heading() },
                        )
                        Spacer(Modifier.height(HomeDecorSpacing.Xs))
                        Text(
                            Strings.profileSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    val settingsInteractionSource = remember { MutableInteractionSource() }
                    val settingsIsPressed by settingsInteractionSource.collectIsPressedAsState()
                    val settingsIsHovered by settingsInteractionSource.collectIsHoveredAsState()
                    val settingsScale by animateFloatAsState(
                        targetValue = if (settingsIsPressed) 0.98f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                        label = "settingsScale",
                    )
                    Surface(
                        onClick = onSettings,
                        shape = HomeDecorShape.Medium,
                        color = if (settingsIsHovered) MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surfaceContainerHigh,
                        interactionSource = settingsInteractionSource,
                        modifier = Modifier
                            .height(40.dp)
                            .scale(settingsScale)
                            .testTag(Strings.TestTags.profileSettingsButton),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Icon(
                                Icons.Rounded.Settings,
                                contentDescription = Strings.settings,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                Strings.settings,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

            if (!signedIn) {
                item("signin-hero") {
                    SignInHeroCard(onSignIn = onSignIn, isDesktop = isDesktop)
                }

                item("account-section-guest") {
                    ProfileSectionWithLabel(
                        label = Strings.accountSection,
                        isDesktop = isDesktop,
                    ) {
                        ProfileRow(
                            icon = Icons.Rounded.Person,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = Strings.accountStatusGuest,
                            subtitle = Strings.accountStatusGuestBody,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Diamond,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.creditsFree,
                            subtitle = Strings.creditsFreeBody,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Star,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.subscriptionFree,
                            subtitle = Strings.subscriptionFreeBody,
                        )
                    }
                }

                item("preferences-section-guest") {
                    ProfileSectionWithLabel(
                        label = Strings.preferencesSection,
                        isDesktop = isDesktop,
                    ) {
                        ProfileRow(
                            icon = Icons.Rounded.Language,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.languageLabel,
                            subtitle = Strings.languageBody,
                            onClick = { showLanguageDialog = true },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Settings,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.themeLabel,
                            subtitle = Strings.themeBody,
                            onClick = { showToast(Strings.toastComingSoon) },
                        )
                    }
                }

                item("billing-section-guest") {
                    ProfileSectionWithLabel(
                        label = Strings.billingSection,
                        isDesktop = isDesktop,
                    ) {
                        ProfileRow(
                            icon = Icons.Rounded.Diamond,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = Strings.buyExtraCredits,
                            subtitle = Strings.buyExtraCreditsBody,
                            onClick = onOpenDiamonds,
                        )
                    }
                }

                item("support-section-guest") {
                    ProfileSectionWithLabel(
                        label = Strings.supportSection,
                        isDesktop = isDesktop,
                    ) {
                        ProfileRow(
                            icon = Icons.Rounded.Help,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.helpCenter,
                            subtitle = Strings.helpCenterBody,
                            onClick = { showToast(Strings.toastComingSoon) },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Mail,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.contactUs,
                            subtitle = Strings.contactUsBody,
                            onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Support%20Request") },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Feedback,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = Strings.rateApp,
                            subtitle = Strings.rateAppBody,
                            onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Feedback") },
                        )
                    }
                }

                item("legal-section-guest") {
                    ProfileSectionWithLabel(
                        label = Strings.legalSection,
                        isDesktop = isDesktop,
                    ) {
                        ProfileRow(
                            icon = Icons.Rounded.Shield,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.termsOfService,
                            subtitle = Strings.termsOfServiceBody,
                            onClick = { showToast(Strings.toastComingSoon) },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Shield,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.privacyPolicyLabel,
                            subtitle = Strings.privacyPolicyBody,
                            onClick = { showToast(Strings.toastComingSoon) },
                        )
                    }
                }
            } else {
                item("profile-hero") {
                    SignedInProfileHero(state = state, isDesktop = isDesktop)
                }

                if (isDesktop) {
                    item("desktop-content") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 800.dp),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                        ) {
                            // Left: Account + Billing
                            Column(modifier = Modifier.weight(1f)) {
                                ProfileSectionLabel(Strings.accountSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Person,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.editProfile,
                                        subtitle = Strings.editProfileBody,
                                        onClick = onSettings,
                                    )
                                }

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.billingSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Diamond,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.myDiamonds,
                                        subtitle = Strings.myDiamondsBody(state.diamonds),
                                        onClick = onOpenDiamonds,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Star,
                                        iconBg = if (state.isPro) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = if (state.isPro) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                                        title = Strings.currentPlan,
                                        subtitle = if (state.isPro) Strings.currentPlanBodyPro else Strings.currentPlanBodyFree,
                                        onClick = onOpenPaywall,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Diamond,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.buyExtraCredits,
                                        subtitle = Strings.buyExtraCreditsBody,
                                        onClick = onOpenDiamonds,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Settings,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.manageBilling,
                                        subtitle = Strings.manageBillingBody,
                                        onClick = { showToast(Strings.toastComingSoon) },
                                    )
                                }
                            }

                            // Right: Preferences + Saved + Support + Legal
                            Column(modifier = Modifier.weight(1f)) {
                                ProfileSectionLabel(Strings.preferencesSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Language,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.languageLabel,
                                        subtitle = Strings.languageBody,
                                        onClick = { showLanguageDialog = true },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Settings,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.themeLabel,
                                        subtitle = Strings.themeBody,
                                        onClick = { showToast(Strings.toastComingSoon) },
                                    )
                                }

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.profileSavedDesigns)
                                ProfileSavedDesignsPreview(
                                    state = state,
                                    onViewAll = onOpenBoard,
                                )

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.supportSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Help,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.helpCenter,
                                        subtitle = Strings.helpCenterBody,
                                        onClick = { showToast(Strings.toastComingSoon) },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Mail,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.contactUs,
                                        subtitle = Strings.contactUsBody,
                                        onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Support%20Request") },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Feedback,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.rateApp,
                                        subtitle = Strings.rateAppBody,
                                        onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Feedback") },
                                    )
                                }

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.legalSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Description,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.termsOfService,
                                        subtitle = Strings.termsOfServiceBody,
                                        onClick = { showToast(Strings.toastComingSoon) },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Shield,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.privacyPolicyLabel,
                                        subtitle = Strings.privacyPolicyBody,
                                        onClick = { showToast(Strings.toastComingSoon) },
                                    )
                                }
                            }
                        }
                    }
                    }
                } else {
                    item("account-section") {
                        ProfileSectionLabel(Strings.accountSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Person,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.editProfile,
                                subtitle = Strings.editProfileBody,
                                onClick = onSettings,
                            )
                        }
                    }

                    item("preferences-section") {
                        ProfileSectionLabel(Strings.preferencesSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Language,
                                iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                title = Strings.languageLabel,
                                subtitle = Strings.languageBody,
                                onClick = { showLanguageDialog = true },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Settings,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.themeLabel,
                                subtitle = Strings.themeBody,
                                onClick = { showToast(Strings.toastComingSoon) },
                            )
                        }
                    }

                    item("billing-section") {
                        ProfileSectionLabel(Strings.billingSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Diamond,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.myDiamonds,
                                subtitle = Strings.myDiamondsBody(state.diamonds),
                                onClick = onOpenDiamonds,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Star,
                                iconBg = if (state.isPro) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                iconTint = if (state.isPro) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                                title = Strings.currentPlan,
                                subtitle = if (state.isPro) Strings.currentPlanBodyPro else Strings.currentPlanBodyFree,
                                onClick = onOpenPaywall,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Diamond,
                                iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                title = Strings.buyExtraCredits,
                                subtitle = Strings.buyExtraCreditsBody,
                                onClick = onOpenDiamonds,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Settings,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.manageBilling,
                                subtitle = Strings.manageBillingBody,
                                onClick = { showToast(Strings.toastComingSoon) },
                            )
                        }
                    }

                    item("saved-designs-preview") {
                        ProfileSectionLabel(Strings.profileSavedDesigns)
                        ProfileSavedDesignsPreview(
                            state = state,
                            onViewAll = onOpenBoard,
                        )
                    }

                    item("support-section") {
                        ProfileSectionLabel(Strings.supportSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Help,
                                iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                title = Strings.helpCenter,
                                subtitle = Strings.helpCenterBody,
                                onClick = { showToast(Strings.toastComingSoon) },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Mail,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.contactUs,
                                subtitle = Strings.contactUsBody,
                                onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Support%20Request") },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Feedback,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.rateApp,
                                subtitle = Strings.rateAppBody,
                                onClick = { onOpenUrl("mailto:support@homedecorai.com?subject=Feedback") },
                            )
                        }
                    }

                    item("legal-section") {
                        ProfileSectionLabel(Strings.legalSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Description,
                                iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                title = Strings.termsOfService,
                                subtitle = Strings.termsOfServiceBody,
                                onClick = { showToast(Strings.toastComingSoon) },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Shield,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.privacyPolicyLabel,
                                subtitle = Strings.privacyPolicyBody,
                                onClick = { showToast(Strings.toastComingSoon) },
                            )
                        }
                    }

                    item("app-info-section") {
                        ProfileSectionLabel(Strings.appInfoSection)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Info,
                                iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                iconTint = MaterialTheme.colorScheme.tertiary,
                                title = Strings.appVersion,
                                subtitle = "1.0.0",
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Info,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.appBuildNumber,
                                subtitle = "1",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SignInHeroCard(onSignIn: () -> Unit, isDesktop: Boolean = false) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.CardInternal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Person,
                        contentDescription = null,
                        modifier = Modifier.size(HomeDecorIconSize.Xl),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                Strings.profileGuestHeadline,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Button(
                onClick = onSignIn,
                shape = HomeDecorShape.Button,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag(Strings.TestTags.profileSignInButton)
                    .semantics { contentDescription = Strings.profileSignInRegister },
            ) {
                Text(
                    Strings.profileSignInRegister,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun ProfileDesignPreviewCard(name: String, style: String) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(160.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f),
                            ),
                        ),
                    ),
            ) {
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(HomeDecorSpacing.Sm)
                        .size(22.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(11.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Column(Modifier.padding(HomeDecorSpacing.Sm)) {
                Text(
                    name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    style,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ProfileStatusCards(
    state: ProfileScreenState,
    onDiamondsClick: () -> Unit,
    onPlanClick: () -> Unit,
    onSavedClick: () -> Unit,
    isDesktop: Boolean = false,
) {
    val isCompact = rememberIsCompact()

    if (isCompact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
        ) {
            StatusCard(
                icon = Icons.Rounded.Diamond,
                label = Strings.profileStatusDiamonds,
                value = state.diamonds.toString(),
                iconTint = HomeDecorExtra.diamondAccent,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth(),
                onClick = onDiamondsClick,
            )
            StatusCard(
                icon = Icons.Rounded.Star,
                label = Strings.profileStatusPlan,
                value = if (state.isPro) "Pro" else Strings.freePlan,
                iconTint = if (state.isPro) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.tertiary,
                containerColor = if (state.isPro) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                },
                modifier = Modifier.fillMaxWidth(),
                onClick = onPlanClick,
            )
            StatusCard(
                icon = Icons.Rounded.FavoriteBorder,
                label = Strings.profileStatusSaved,
                value = state.favoritesCount.toString(),
                iconTint = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth(),
                onClick = onSavedClick,
            )
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(if (isDesktop) HomeDecorSpacing.Md else HomeDecorSpacing.Sm),
        ) {
            StatusCard(
                icon = Icons.Rounded.Diamond,
                label = Strings.profileStatusDiamonds,
                value = state.diamonds.toString(),
                iconTint = HomeDecorExtra.diamondAccent,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                modifier = Modifier.weight(1f),
                onClick = onDiamondsClick,
            )
            StatusCard(
                icon = Icons.Rounded.Star,
                label = Strings.profileStatusPlan,
                value = if (state.isPro) "Pro" else Strings.freePlan,
                iconTint = if (state.isPro) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.tertiary,
                containerColor = if (state.isPro) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                },
                modifier = Modifier.weight(1f),
                onClick = onPlanClick,
            )
            StatusCard(
                icon = Icons.Rounded.FavoriteBorder,
                label = Strings.profileStatusSaved,
                value = state.favoritesCount.toString(),
                iconTint = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                modifier = Modifier.weight(1f),
                onClick = onSavedClick,
            )
        }
    }
}

@Composable
private fun StatusCard(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Surface(
        shape = HomeDecorShape.CardLarge,
        color = if (isHovered) containerColor.copy(alpha = 0.8f) else containerColor,
        modifier = modifier
            .minimumTouchTarget()
            .testTag(Strings.formatTestTag(Strings.TestTags.profileStatusCard, label))
            .semantics {
                contentDescription = "$label: $value"
                role = Role.Button
            }
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(HomeDecorIconSize.Medium),
                tint = iconTint,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Xxs))
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SignedInProfileHero(state: ProfileScreenState, isDesktop: Boolean = false) {
    Surface(
        shape = HomeDecorShape.ExtraExtraLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(
                horizontal = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.CardInternal,
                vertical = HomeDecorSpacing.Xl,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
        ) {
            Box {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(72.dp),
                ) {
                    val displayName = state.signedInName
                        ?.takeIf { it.isNotBlank() }
                        ?: state.signedInEmail?.substringBefore('@')?.takeIf { it.isNotBlank() }
                        ?: Strings.guestExplorer
                    val initials = displayName.take(2).uppercase()
                    Text(
                        initials,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(1.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                val displayName = state.signedInName
                    ?.takeIf { it.isNotBlank() }
                    ?: state.signedInEmail?.substringBefore('@')?.takeIf { it.isNotBlank() }
                    ?: Strings.guestExplorer
                Text(
                    displayName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    state.signedInEmail ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Sm))
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = if (state.isPro) {
                        HomeDecorExtra.premiumGold.copy(alpha = 0.12f)
                    } else {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    },
                ) {
                    Row(
                        Modifier.padding(
                            horizontal = HomeDecorSpacing.Md,
                            vertical = HomeDecorSpacing.Xs,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                    ) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = if (state.isPro) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            if (state.isPro) Strings.profileProMember else Strings.freePlan,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.isPro) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSavedDesignsPreview(
    state: ProfileScreenState,
    onViewAll: () -> Unit,
) {
    val savedInteractionSource = remember { MutableInteractionSource() }
    val savedIsPressed by savedInteractionSource.collectIsPressedAsState()
    val savedIsHovered by savedInteractionSource.collectIsHoveredAsState()
    val savedScale by animateFloatAsState(
        targetValue = if (savedIsPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "savedScale",
    )

    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            if (state.favoritesCount == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(HomeDecorSpacing.Xl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    Surface(
                        shape = HomeDecorShape.CardLarge,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Large),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Text(
                        Strings.profileNoDesignsYet,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        Strings.boardEmptyGeneratedBody,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(HomeDecorSpacing.Xs))
                    Surface(
                        shape = HomeDecorShape.Button,
                        color = if (savedIsHovered) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(interactionSource = savedInteractionSource, indication = null, onClick = onViewAll)
                            .scale(savedScale),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                        ) {
                            Icon(
                                Icons.Rounded.Explore,
                                contentDescription = null,
                                modifier = Modifier.size(HomeDecorIconSize.Small),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                            Text(
                                Strings.savedDesignsEmptyCta,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(HomeDecorSpacing.Md),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    val displayItems = state.savedDesigns.take(3)
                    items(displayItems.size) { index ->
                        val item = displayItems[index]
                        ProfileDesignPreviewCard(
                            name = item.roomType.ifBlank { item.style }.ifBlank { item.toolTitle }.ifBlank { "Design ${index + 1}" },
                            style = item.style.ifBlank { item.roomType }.ifBlank { "Saved" },
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = savedInteractionSource, indication = null, onClick = onViewAll)
                        .scale(savedScale)
                        .padding(HomeDecorSpacing.Md),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        Strings.profileViewAllDesigns,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSectionLabel(label: String) {
    Text(
        label,
        modifier = Modifier
            .padding(horizontal = HomeDecorSpacing.Xs)
            .semantics { heading() },
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun ProfileSectionWithLabel(
    label: String,
    isDesktop: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isDesktop) Alignment.TopCenter else Alignment.TopStart,
    ) {
        Column(
            modifier = if (isDesktop) Modifier.widthIn(max = 800.dp) else Modifier,
        ) {
            ProfileSectionLabel(label)
            SettingsCard(maxWidth = if (isDesktop) 800.dp else null) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsCard(
    maxWidth: Dp? = null,
    content: @Composable () -> Unit,
) {
    val cardModifier = if (maxWidth != null) {
        Modifier.widthIn(max = maxWidth).fillMaxWidth()
    } else {
        Modifier.fillMaxWidth()
    }
    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = cardModifier,
    ) {
        Column(Modifier.padding(vertical = HomeDecorSpacing.Sm)) {
            content()
        }
    }
}

@Composable
private fun ProfileRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
) {
    val rowInteractionSource = remember { MutableInteractionSource() }
    val rowIsPressed by rowInteractionSource.collectIsPressedAsState()
    val rowScale by animateFloatAsState(
        targetValue = if (rowIsPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "rowScale",
    )
    ListItem(
        headlineContent = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = if (subtitle.isNotBlank()) {
            {
                Text(
                    subtitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        } else null,
        leadingContent = {
            Surface(
                shape = HomeDecorShape.Badge,
                color = iconBg,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(HomeDecorIconSize.Medium),
                    )
                }
            }
        },
        trailingContent = if (onClick != null) {
            {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(HomeDecorIconSize.Small),
                )
            }
        } else null,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .testTag(Strings.formatTestTag(Strings.TestTags.profileRow, title))
            .semantics {
                contentDescription = "$title. $subtitle"
                if (onClick != null) role = Role.Button
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = rowInteractionSource, indication = null, onClick = onClick)
                } else {
                    Modifier
                }
            )
            .scale(rowScale),
    )
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Lg),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    )
}
