package com.ismail.homedecorai.ui.paywall

import androidx.compose.runtime.Composable
import com.ismail.homedecorai.model.HomeDecorUiState

@Composable
fun AndroidPaywallWrapper(
    state: HomeDecorUiState,
    onClose: () -> Unit,
    onSubscription: (String, String, String, Double?, Double?) -> Unit,
    onRetrySync: () -> Unit,
    onStore: () -> Unit,
) {
    PaywallSheet(
        state = state,
        onClose = onClose,
        onSubscription = onSubscription,
        onRetrySync = onRetrySync,
        onStore = onStore,
    )
}
