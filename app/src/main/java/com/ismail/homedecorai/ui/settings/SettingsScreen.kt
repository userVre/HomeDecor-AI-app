package com.ismail.homedecorai.ui.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.model.HomeDecorUiState
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.R
import com.ismail.homedecorai.ui.dialogs.DeleteAccountDialog
import com.ismail.homedecorai.ui.dialogs.FeedbackDialog
import com.ismail.homedecorai.ui.theme.StudioBlue
import com.ismail.homedecorai.ui.theme.StudioCanvas
import com.ismail.homedecorai.ui.theme.StudioLine
import com.ismail.homedecorai.ui.theme.StudioRose
import com.ismail.homedecorai.ui.utility.openGooglePlayReview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var languageSheetVisible by remember { mutableStateOf(false) }
    var feedbackDialogVisible by remember { mutableStateOf(false) }
    var deleteDialogVisible by remember { mutableStateOf(false) }

    if (languageSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { languageSheetVisible = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            LanguagePickerContent(
                currentLanguageTag = currentLanguageTag,
                onLanguageSelected = { languageTag ->
                    onLanguageSelected(languageTag)
                    val localizedContext = AppLocale.wrap(context, languageTag)
                    Toast.makeText(
                        localizedContext,
                        localizedContext.getString(R.string.toast_language_selected),
                        Toast.LENGTH_LONG,
                    ).show()
                    scope.launch {
                        sheetState.hide()
                        languageSheetVisible = false
                    }
                },
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.settings),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = StudioCanvas),
                )
            },
            containerColor = StudioCanvas,
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                item {
                    SettingsItemRow(
                        icon = Icons.Rounded.Language,
                        title = stringResource(R.string.language),
                        onClick = { languageSheetVisible = true },
                    )
                    SettingsFullDivider()
                }
                item {
                    SettingsItemRow(
                        icon = Icons.Rounded.Star,
                        title = stringResource(R.string.rate_us),
                        onClick = { openGooglePlayReview(context) },
                    )
                    SettingsFullDivider()
                }
                item {
                    SettingsItemRow(
                        icon = Icons.AutoMirrored.Rounded.Help,
                        title = stringResource(R.string.contact_support),
                        onClick = { feedbackDialogVisible = true },
                    )
                }
                item {
                    SettingsFullDivider()
                }
                item {
                    SettingsItemRow(
                        icon = Icons.Rounded.Delete,
                        title = stringResource(R.string.delete_information),
                        titleColor = StudioRose,
                        iconTint = StudioRose,
                        onClick = { deleteDialogVisible = true },
                    )
                }
                item {
                    Text(
                        stringResource(R.string.version_label, "1.0.0"),
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
            .semantics {
                role = Role.Button
                contentDescription = title
            }
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(28.dp),
        )
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

@Composable
private fun LanguagePickerContent(
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        item {
            Text(
                stringResource(R.string.language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
        }
        item {
            val systemSelected = currentLanguageTag == AppLocale.SYSTEM_LANGUAGE_TAG
            LanguageRow(
                label = stringResource(R.string.language_system_default),
                selected = systemSelected,
                onClick = { onLanguageSelected(AppLocale.SYSTEM_LANGUAGE_TAG) },
            )
            SettingsFullDivider()
        }
        items(AppLocale.supportedLanguages) { language ->
            val selected = language.tag == currentLanguageTag
            LanguageRow(
                label = stringResource(language.labelRes),
                selected = selected,
                onClick = { onLanguageSelected(language.tag) },
            )
            SettingsFullDivider()
        }
        item {
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.padding(bottom = 16.dp)
            )
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
        stringResource(R.string.a11y_language_selected_format, label)
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
                contentDescription = stringResource(R.string.a11y_check_icon),
                tint = StudioBlue,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
