package com.ismail.homedecorai.ui.profile

import androidx.compose.runtime.Composable
import com.ismail.homedecorai.HomeDecorViewModel
import com.ismail.homedecorai.model.HomeDecorUiState

@Composable
fun ProfileScreen(
    state: HomeDecorUiState,
    viewModel: HomeDecorViewModel,
) {
    SharedProfileScreen(
        state = ProfileScreenState(
            isGuest = state.viewer.isGuest,
            signedInName = state.signedInName,
            signedInEmail = state.signedInEmail,
            diamonds = state.diamonds,
            isPro = state.isPro,
            favoritesCount = state.workspace.favorites.size,
        ),
        onSettings = viewModel::openSettings,
        onSignIn = viewModel::openAuth,
        onOpenDiamonds = viewModel::openDiamondStore,
        onOpenPaywall = viewModel::openPaywall,
    )
}
