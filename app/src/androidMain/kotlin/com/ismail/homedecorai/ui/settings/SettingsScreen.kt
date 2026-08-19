package com.ismail.homedecorai.ui.settings

import androidx.compose.runtime.Composable
import com.ismail.homedecorai.AppLocale
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.HomeDecorUiState

@Composable
fun SettingsScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit,
) {
    val supportedLanguages = AppLocale.supportedLanguages.map {
        SettingsLanguage(tag = it.tag, label = it.labelRes.toString())
    }
    SharedSettingsScreen(
        state = SettingsScreenState(
            versionName = "1.0.0",
            settingsBusy = state.settingsBusy,
        ),
        currentLanguageTag = currentLanguageTag,
        supportedLanguages = supportedLanguages,
        onLanguageSelected = onLanguageSelected,
        onRateUs = { },
        onContactSupport = { },
        onDeleteInformation = { },
        onSubmitFeedback = viewModel::submitSettingsFeedback,
        onConfirmDelete = viewModel::deleteAccountData,
    )
}
