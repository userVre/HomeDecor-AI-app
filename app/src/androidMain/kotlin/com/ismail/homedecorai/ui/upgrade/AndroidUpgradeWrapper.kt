package com.ismail.homedecorai.ui.upgrade

import androidx.compose.runtime.Composable
import com.ismail.homedecorai.model.HomeDecorUiState

@Composable
fun AndroidUpgradeWrapper(
    state: HomeDecorUiState,
    onOpenPaywall: () -> Unit,
) {
    UpgradeScreen(
        state = state,
        onOpenPaywall = onOpenPaywall,
    )
}
