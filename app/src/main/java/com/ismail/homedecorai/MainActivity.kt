package com.ismail.homedecorai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ismail.homedecorai.ui.HomeDecorApp

class MainActivity : ComponentActivity() {
    private val viewModel: HomeDecorViewModel by viewModels {
        HomeDecorViewModel.Factory(
            repository = HomeDecorRepository((application as HomeDecorApplication).services),
            context = this,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.WHITE,
                android.graphics.Color.WHITE,
            ),
        )
        setContent {
            HomeDecorApp(viewModel = viewModel)
        }
    }
}
