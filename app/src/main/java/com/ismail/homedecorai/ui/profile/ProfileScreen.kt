package com.ismail.homedecorai.ui.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.MainTab
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.dialogs.FeedbackDialog
import com.ismail.homedecorai.ui.dialogs.LanguagePickerDialog
import com.ismail.homedecorai.ui.theme.*
import com.ismail.homedecorai.ui.utility.appUrl
import com.ismail.homedecorai.ui.utility.openAuth
import com.ismail.homedecorai.ui.utility.openUrlSafely
import com.revenuecat.purchases.Purchases

@Composable
fun ProfileScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val signedIn = !state.viewer.isGuest || state.signedInName != null
    val openRealAuth = { openAuth(context) }
    var languagePickerVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(StudioCanvas)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = HomeDecorSpacing.Lg, end = HomeDecorSpacing.Lg, top = HomeDecorSpacing.Sm, bottom = navBarBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(HomeDecorSpacing.SectionGap),
        ) {
            item("profile-title") {
                Text(
                    stringResource(R.string.my_profile_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }

            if (!signedIn) {
                item("signin-hero") {
                    SignInHeroCard(
                        onSignIn = openRealAuth,
                        onGoogle = openRealAuth,
                    )
                }
            } else {
                item("profile-hero") {
                    SignedInProfileHero(
                        state = state,
                    )
                }
                item("account-section") {
                    ProfileSectionLabel(stringResource(R.string.account_section))
                    SettingsCard {
                        ProfileRow(
                            icon = Icons.Rounded.Person,
                            iconBg = StudioProContainer,
                            iconTint = StudioGold,
                            title = stringResource(R.string.edit_profile),
                            subtitle = stringResource(R.string.edit_profile_body),
                            onClick = viewModel::openSettings,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Shield,
                            iconBg = StudioErrorContainer,
                            iconTint = StudioRose,
                            title = stringResource(R.string.privacy_security),
                            subtitle = stringResource(R.string.privacy_security_body),
                            onClick = viewModel::openSettings,
                        )
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.Rounded.Diamond,
                            iconBg = StudioPrimaryContainer,
                            iconTint = StudioBlue,
                            title = stringResource(R.string.my_diamonds),
                            subtitle = stringResource(R.string.my_diamonds_body, state.diamonds),
                            onClick = viewModel::openDiamondStore,
                        )
                    }
                }
                item("subscription-section") {
                    ProfileSectionLabel(stringResource(R.string.subscription_status))
                    SettingsCard {
                        ProfileRow(
                            icon = Icons.Rounded.Star,
                            iconBg = if (state.isPro) StudioSuccessContainer else StudioProContainer,
                            iconTint = if (state.isPro) StudioGreen else StudioGold,
                            title = stringResource(R.string.current_plan),
                            subtitle = if (state.isPro) stringResource(R.string.current_plan_body_pro) else stringResource(R.string.current_plan_body_free),
                            onClick = viewModel::openPaywall,
                        )
                    }
                }
            }

            item("settings-section") {
                ProfileSectionLabel(stringResource(R.string.settings_section))
                SettingsCard {
                    ProfileRow(
                        icon = Icons.Rounded.Language,
                        iconBg = StudioPrimaryContainer,
                        iconTint = StudioBlue,
                        title = stringResource(R.string.language),
                        subtitle = AppLocale.labelFor(context, currentLanguageTag),
                        onClick = { languagePickerVisible = true },
                    )
                    ProfileDivider()
                    ProfileRow(
                        icon = Icons.Rounded.Refresh,
                        iconBg = StudioSuccessContainer,
                        iconTint = StudioGreen,
                        title = stringResource(R.string.restore_purchases),
                        subtitle = stringResource(R.string.restore_purchases_subtitle),
                        onClick = { viewModel.openPaywall() },
                    )
                    ProfileDivider()
                    ProfileRow(
                        icon = Icons.Rounded.Policy,
                        iconBg = StudioPrimaryContainer,
                        iconTint = StudioBlue,
                        title = stringResource(R.string.terms),
                        subtitle = stringResource(R.string.terms_subtitle),
                        onClick = { openUrlSafely(context, appUrl("/terms")) },
                    )
                    ProfileDivider()
                    ProfileRow(
                        icon = Icons.Rounded.Policy,
                        iconBg = StudioPrimaryContainer,
                        iconTint = StudioBlue,
                        title = stringResource(R.string.privacy_policy),
                        subtitle = stringResource(R.string.privacy_subtitle),
                        onClick = { openUrlSafely(context, appUrl("/privacy")) },
                    )
                    ProfileDivider()
                    ProfileRow(
                        icon = Icons.Rounded.RateReview,
                        iconBg = StudioSuccessContainer,
                        iconTint = StudioGreen,
                        title = stringResource(R.string.feedback),
                        subtitle = stringResource(R.string.feedback_subtitle),
                        onClick = { feedbackDialogVisible = true },
                    )
                    ProfileDivider()
                    ProfileRow(
                        icon = Icons.AutoMirrored.Rounded.Help,
                        iconBg = StudioPrimaryContainer,
                        iconTint = StudioBlue,
                        title = stringResource(R.string.help_faq),
                        subtitle = stringResource(R.string.help_faq_body),
                        onClick = { openUrlSafely(context, appUrl("/faq")) },
                    )
                    if (signedIn) {
                        ProfileDivider()
                        ProfileRow(
                            icon = Icons.AutoMirrored.Rounded.Logout,
                            iconBg = StudioErrorContainer,
                            iconTint = StudioRose,
                            title = stringResource(R.string.sign_out),
                            subtitle = "",
                            onClick = viewModel::logOut,
                        )
                    }
                }
            }
        }
    }

    if (languagePickerVisible) {
        LanguagePickerDialog(
            currentLanguageTag = currentLanguageTag,
            onLanguageSelected = {
                onLanguageSelected(it)
                languagePickerVisible = false
            },
            onDismiss = { languagePickerVisible = false },
        )
    }

    if (feedbackDialogVisible) {
        FeedbackDialog(
            busy = false,
            onSubmit = { message ->
                viewModel.submitSettingsFeedback(message)
                feedbackDialogVisible = false
            },
            onDismiss = { feedbackDialogVisible = false },
        )
    }
}

@Composable
private fun SignInHeroCard(
    onSignIn: () -> Unit,
    onGoogle: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.5f.dp, StudioLine),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(HomeDecorSpacing.CardInternal),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.profile_sign_in_body),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
            )
            Spacer(Modifier.height(HomeDecorSpacing.Lg))
            Button(
                onClick = onSignIn,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudioBrownBtn),
                modifier = Modifier.fillMaxWidth().height(HomeDecorSpacing.ButtonHeight),
            ) {
                Text(stringResource(R.string.sign_in))
            }
            Spacer(Modifier.height(HomeDecorSpacing.Md))
            OutlinedButton(
                onClick = onGoogle,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5f.dp, StudioLine),
                modifier = Modifier.fillMaxWidth().height(HomeDecorSpacing.ButtonHeight),
            ) {
                Text(stringResource(R.string.continue_with_google))
            }
        }
    }
}

@Composable
private fun SignedInProfileHero(
    state: HomeDecorUiState,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.5f.dp, StudioLine),
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
                    color = StudioPrimaryContainer,
                    modifier = Modifier.size(72.dp),
                ) {
                    val initials = (state.signedInName ?: stringResource(R.string.initials_fallback)).take(2).uppercase()
                    Text(
                        initials,
                        modifier = Modifier.fillMaxSize().padding(1.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = StudioBlue,
                        textAlign = TextAlign.Center,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StudioGold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 2.dp, bottom = 2.dp),
                ) {
                    Text(
                        stringResource(R.string.free_badge),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                    )
                }
            }
            Column {
                Text(
                    state.signedInName ?: stringResource(R.string.account_connected),
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
                    shape = RoundedCornerShape(12.dp),
                    color = StudioProContainer,
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(Icons.Rounded.Star, contentDescription = null, modifier = Modifier.size(12.dp), tint = StudioGold)
                        Text(
                            stringResource(R.string.free_plan),
                            style = MaterialTheme.typography.labelSmall,
                            color = StudioGold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpgradeBanner(
    onUpgrade: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioBrownBtn,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier.padding(HomeDecorSpacing.CardInternal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(HomeDecorSpacing.Sm)) {
                    Icon(Icons.Rounded.Star, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFFF0D98A))
                    Text(
                        stringResource(R.string.go_premium),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                    )
                }
                Spacer(Modifier.height(HomeDecorSpacing.Xs))
                Text(
                    stringResource(R.string.go_premium_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
            Surface(
                onClick = onUpgrade,
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF0D98A),
                modifier = Modifier.semantics {
                    contentDescription = "Upgrade to PRO"
                    role = Role.Button
                },
            ) {
                Text(
                    stringResource(R.string.upgrade),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = StudioBrownDark,
                )
            }
        }
    }
}

@Composable
private fun ProfileSectionLabel(label: String) {
    Text(
        label,
        modifier = Modifier.padding(horizontal = 4.dp),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = StudioPaper,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, StudioLine),
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
        headlineContent = {
            Text(title)
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
            Surface(        shape = RoundedCornerShape(8.dp), color = iconBg) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(8.dp).size(20.dp),
                )
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
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .minimumTouchTarget()
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
    )
}

@Composable
private fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HomeDecorSpacing.Base)
            .height(1.dp)
            .background(StudioMist),
    )
}
