package com.ismail.homedecorai.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.Strings
import com.ismail.homedecorai.ui.rememberIsDesktop
import com.ismail.homedecorai.ui.theme.*

data class ProfileScreenState(
    val isGuest: Boolean = true,
    val signedInName: String? = null,
    val signedInEmail: String? = null,
    val diamonds: Int = 0,
    val isPro: Boolean = false,
    val favoritesCount: Int = 0,
)

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
    val signedIn = !state.isGuest || state.signedInName != null
    val isDesktop = rememberIsDesktop()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(Strings.TestTags.profileScreen),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                start = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base,
                end = if (isDesktop) HomeDecorSpacing.Xl else HomeDecorSpacing.Base,
                top = HomeDecorSpacing.Sm,
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
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.semantics { heading() },
                        )
                        Spacer(Modifier.height(HomeDecorSpacing.Xs))
                        Text(
                            Strings.profileSubtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(
                        onClick = onSettings,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag(Strings.TestTags.profileSettingsButton),
                    ) {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = Strings.settings,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            if (!signedIn) {
                item("signin-hero") {
                    SignInHeroCard(onSignIn = onSignIn, isDesktop = isDesktop)
                }

                item("preferences-section-guest") {
                    ProfileSectionLabel(Strings.preferencesSection)
                    SettingsCard {
                        ProfileRow(
                            icon = Icons.Rounded.Language,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.languageLabel,
                            subtitle = Strings.languageBody,
                            onClick = onSettings,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Brightness6,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = Strings.themeLabel,
                            subtitle = Strings.themeBody,
                            onClick = onSettings,
                        )
                    }
                }

                item("support-section-guest") {
                    ProfileSectionLabel(Strings.supportSection)
                    SettingsCard {
                        ProfileRow(
                            icon = Icons.Rounded.Help,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.helpCenter,
                            subtitle = Strings.helpCenterBody,
                            onClick = { onOpenUrl("https://homedecorai.com/help") },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Mail,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.contactUs,
                            subtitle = Strings.contactUsBody,
                            onClick = onSettings,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.StarRate,
                            iconBg = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = Strings.rateApp,
                            subtitle = Strings.rateAppBody,
                            onClick = { onOpenUrl("https://homedecorai.com/rate") },
                        )
                    }
                }

                item("legal-section-guest") {
                    ProfileSectionLabel(Strings.legalSection)
                    SettingsCard {
                        ProfileRow(
                            icon = Icons.Rounded.Description,
                            iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                            iconTint = MaterialTheme.colorScheme.tertiary,
                            title = Strings.termsOfService,
                            subtitle = Strings.termsOfServiceBody,
                            onClick = { onOpenUrl("https://homedecorai.com/terms") },
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Shield,
                            iconBg = MaterialTheme.colorScheme.secondaryContainer,
                            iconTint = MaterialTheme.colorScheme.secondary,
                            title = Strings.privacyPolicyLabel,
                            subtitle = Strings.privacyPolicyBody,
                            onClick = { onOpenUrl("https://homedecorai.com/privacy") },
                        )
                    }
                }
            } else {
                item("profile-hero") {
                    SignedInProfileHero(state = state, isDesktop = isDesktop)
                }

                item("status-cards") {
                    ProfileStatusCards(
                        state = state,
                        onDiamondsClick = onOpenDiamonds,
                        onPlanClick = onOpenPaywall,
                        onSavedClick = onOpenBoard,
                        isDesktop = isDesktop,
                    )
                }

                if (isDesktop) {
                    item("desktop-content") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
                        ) {
                            // Left: Account + Subscription Sections
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
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Diamond,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.myDiamonds,
                                        subtitle = Strings.myDiamondsBody(state.diamonds),
                                        onClick = onOpenDiamonds,
                                    )
                                }

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.subscriptionStatus)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Star,
                                        iconBg = if (state.isPro) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = if (state.isPro) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                                        title = Strings.currentPlan,
                                        subtitle = if (state.isPro) Strings.currentPlanBodyPro else Strings.currentPlanBodyFree,
                                        onClick = onOpenPaywall,
                                    )
                                }
                            }

                            // Right: Saved Designs + Preferences + Support + Legal
                            Column(modifier = Modifier.weight(1f)) {
                                ProfileSectionLabel(Strings.profileSavedDesigns)
                                ProfileSavedDesignsPreview(
                                    state = state,
                                    onViewAll = onOpenBoard,
                                )

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.preferencesSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Language,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.languageLabel,
                                        subtitle = Strings.languageBody,
                                        onClick = onSettings,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Notifications,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.notificationsLabel,
                                        subtitle = Strings.notificationsBody,
                                        onClick = onSettings,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Brightness6,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.themeLabel,
                                        subtitle = Strings.themeBody,
                                        onClick = onSettings,
                                    )
                                }

                                Spacer(Modifier.height(HomeDecorSpacing.Base))

                                ProfileSectionLabel(Strings.supportSection)
                                SettingsCard {
                                    ProfileRow(
                                        icon = Icons.Rounded.Help,
                                        iconBg = MaterialTheme.colorScheme.tertiaryContainer,
                                        iconTint = MaterialTheme.colorScheme.tertiary,
                                        title = Strings.helpCenter,
                                        subtitle = Strings.helpCenterBody,
                                        onClick = { onOpenUrl("https://homedecorai.com/help") },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Mail,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.contactUs,
                                        subtitle = Strings.contactUsBody,
                                        onClick = onSettings,
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.StarRate,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.rateApp,
                                        subtitle = Strings.rateAppBody,
                                        onClick = { onOpenUrl("https://homedecorai.com/rate") },
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
                                        onClick = { onOpenUrl("https://homedecorai.com/terms") },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Shield,
                                        iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                        iconTint = MaterialTheme.colorScheme.secondary,
                                        title = Strings.privacyPolicyLabel,
                                        subtitle = Strings.privacyPolicyBody,
                                        onClick = { onOpenUrl("https://homedecorai.com/privacy") },
                                    )
                                    ProfileDivider()
                                    ProfileRow(
                                        icon = Icons.Rounded.Code,
                                        iconBg = MaterialTheme.colorScheme.primaryContainer,
                                        iconTint = MaterialTheme.colorScheme.primary,
                                        title = Strings.openSourceLicenses,
                                        subtitle = Strings.openSourceLicensesBody,
                                        onClick = { onOpenUrl("https://homedecorai.com/licenses") },
                                    )
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
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Diamond,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.myDiamonds,
                                subtitle = Strings.myDiamondsBody(state.diamonds),
                                onClick = onOpenDiamonds,
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

                    item("subscription-section") {
                        ProfileSectionLabel(Strings.subscriptionStatus)
                        SettingsCard {
                            ProfileRow(
                                icon = Icons.Rounded.Star,
                                iconBg = if (state.isPro) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                iconTint = if (state.isPro) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                                title = Strings.currentPlan,
                                subtitle = if (state.isPro) Strings.currentPlanBodyPro else Strings.currentPlanBodyFree,
                                onClick = onOpenPaywall,
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
                                onClick = onSettings,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Notifications,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.notificationsLabel,
                                subtitle = Strings.notificationsBody,
                                onClick = onSettings,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Brightness6,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.themeLabel,
                                subtitle = Strings.themeBody,
                                onClick = onSettings,
                            )
                        }
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
                                onClick = { onOpenUrl("https://homedecorai.com/help") },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Mail,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.contactUs,
                                subtitle = Strings.contactUsBody,
                                onClick = onSettings,
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.StarRate,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.rateApp,
                                subtitle = Strings.rateAppBody,
                                onClick = { onOpenUrl("https://homedecorai.com/rate") },
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
                                onClick = { onOpenUrl("https://homedecorai.com/terms") },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Shield,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.privacyPolicyLabel,
                                subtitle = Strings.privacyPolicyBody,
                                onClick = { onOpenUrl("https://homedecorai.com/privacy") },
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Code,
                                iconBg = MaterialTheme.colorScheme.primaryContainer,
                                iconTint = MaterialTheme.colorScheme.primary,
                                title = Strings.openSourceLicenses,
                                subtitle = Strings.openSourceLicensesBody,
                                onClick = { onOpenUrl("https://homedecorai.com/licenses") },
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
                                onClick = {},
                            )
                            ProfileDivider()
                            ProfileRow(
                                icon = Icons.Rounded.Dashboard,
                                iconBg = MaterialTheme.colorScheme.secondaryContainer,
                                iconTint = MaterialTheme.colorScheme.secondary,
                                title = Strings.appBuildNumber,
                                subtitle = "1",
                                onClick = {},
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
        if (isDesktop) {
            // Desktop: 2-column — headline + benefit grid | preview cards
            Row(
                Modifier.padding(HomeDecorSpacing.CardInternal),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xl),
            ) {
                // Left column: headline + benefits + CTA
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
                ) {
                    Text(
                        Strings.boardGuestHeadline,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        Strings.profileSignInBody,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    // 2x2 benefit grid
                    Column(
                        verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ProfileBenefitCard(
                                icon = Icons.Rounded.FavoriteBorder,
                                title = Strings.profileGuestBenefit1Title,
                                body = Strings.profileGuestBenefit1Body,
                                modifier = Modifier.weight(1f),
                            )
                            ProfileBenefitCard(
                                icon = Icons.Rounded.PhoneAndroid,
                                title = Strings.profileGuestBenefit2Title,
                                body = Strings.profileGuestBenefit2Body,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ProfileBenefitCard(
                                icon = Icons.Rounded.Diamond,
                                title = Strings.profileGuestBenefit3Title,
                                body = Strings.profileGuestBenefit3Body,
                                modifier = Modifier.weight(1f),
                            )
                            ProfileBenefitCard(
                                icon = Icons.Rounded.Star,
                                title = Strings.profileGuestBenefit4Title,
                                body = Strings.profileGuestBenefit4Body,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    Button(
                        onClick = onSignIn,
                        shape = HomeDecorShape.Button,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .width(220.dp)
                            .height(HomeDecorSpacing.ButtonHeight)
                            .testTag(Strings.TestTags.profileSignInButton),
                    ) {
                        Text(
                            Strings.boardGuestCta,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                // Right column: design preview cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    ProfileDesignPreviewCard("Living Room", "Scandinavian")
                    ProfileDesignPreviewCard("Bedroom", "Bohemian")
                    ProfileDesignPreviewCard("Kitchen", "Minimalist")
                }
            }
        } else {
            // Mobile: stacked — preview row, headline, benefits, CTA
            Column(
                Modifier.padding(HomeDecorSpacing.CardInternal),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            ) {
                // Preview designs row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    item { ProfileDesignPreviewCard("Living Room", "Scandinavian") }
                    item { ProfileDesignPreviewCard("Bedroom", "Bohemian") }
                }

                Text(
                    Strings.boardGuestHeadline,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    Strings.profileSignInBody,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                )

                // Compact benefit chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ProfileBenefitChip(
                        icon = Icons.Rounded.FavoriteBorder,
                        label = Strings.profileGuestBenefit1Title,
                        modifier = Modifier.weight(1f),
                    )
                    ProfileBenefitChip(
                        icon = Icons.Rounded.PhoneAndroid,
                        label = "Cross-device",
                        modifier = Modifier.weight(1f),
                    )
                    ProfileBenefitChip(
                        icon = Icons.Rounded.Star,
                        label = "History",
                        modifier = Modifier.weight(1f),
                    )
                }

                Button(
                    onClick = onSignIn,
                    shape = HomeDecorShape.Button,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeDecorSpacing.ButtonHeight)
                        .testTag(Strings.TestTags.profileSignInButton),
                ) {
                    Text(
                        Strings.boardGuestCta,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileBenefitCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
            verticalAlignment = Alignment.Top,
        ) {
            Surface(
                shape = HomeDecorShape.Badge,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(36.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ProfileBenefitChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = HomeDecorShape.Chip,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
                    .clip(HomeDecorShape.Large)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.4f),
                            ),
                        ),
                    ),
            ) {
                // Room silhouette shapes
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 40.dp)
                        .size(50.dp, 24.dp)
                        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 68.dp, top = 44.dp)
                        .size(34.dp, 18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 112.dp, top = 24.dp)
                        .size(18.dp, 34.dp)
                        .clip(RoundedCornerShape(topStart = 9.dp, topEnd = 9.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                )
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 66.dp)
                        .size(110.dp, 10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)),
                )

                // AI sparkle badge
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    if (isDesktop) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Md),
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
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
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
        shape = HomeDecorShape.Card,
        color = if (isHovered) containerColor.copy(alpha = 0.8f) else containerColor,
        modifier = modifier
            .minimumTouchTarget()
            .testTag(Strings.formatTestTag(Strings.TestTags.profileStatusCard, label))
            .semantics { role = Role.Button }
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(HomeDecorSpacing.Md),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTint,
            )
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            Modifier.padding(HomeDecorSpacing.CardInternal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Base),
        ) {
            Box {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(72.dp),
                ) {
                    val initials = (state.signedInName ?: Strings.initialsFallback).take(2).uppercase()
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
                Surface(
                    shape = HomeDecorShape.Badge,
                    color = if (state.isPro) HomeDecorExtra.premiumGold else MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 2.dp, bottom = 2.dp),
                ) {
                    Text(
                        if (state.isPro) "PRO" else Strings.freeBadge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
            }
            Column {
                Text(
                    state.signedInName ?: Strings.accountConnected,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    state.signedInEmail ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(HomeDecorSpacing.Xs))
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
    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            if (state.favoritesCount == 0) {
                // Empty state
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
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Text(
                        Strings.profileNoDesignsYet,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(HomeDecorSpacing.Xs))
                    Surface(
                        shape = HomeDecorShape.Button,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onViewAll),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = HomeDecorSpacing.Md, vertical = HomeDecorSpacing.Sm),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Xs),
                        ) {
                            Icon(
                                Icons.Rounded.Explore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
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
                // Design preview grid
                LazyRow(
                    contentPadding = PaddingValues(HomeDecorSpacing.Md),
                    horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm),
                ) {
                    items(3.coerceAtMost(state.favoritesCount)) { index ->
                        ProfileDesignPreviewCard(
                            name = "Design ${index + 1}",
                            style = "Saved",
                        )
                    }
                }

                // View all button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onViewAll)
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
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Xs),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Surface(
        shape = HomeDecorShape.CardLarge,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(vertical = HomeDecorSpacing.CardInternal)) {
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
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title) },
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
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp),
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .testTag(Strings.formatTestTag(Strings.TestTags.profileRow, title))
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
    )
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = HomeDecorSpacing.Base),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    )
}
