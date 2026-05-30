package com.ismail.homedecorai

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.ismail.homedecorai.ui.HomeDecorApp

class MainActivity : ComponentActivity() {
    private val viewModel: HomeDecorViewModel by viewModels {
        HomeDecorViewModel.Factory(
            repository = HomeDecorRepository((application as HomeDecorApplication).services, this),
            context = this,
        )
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocale.wrap(newBase, AppLocale.readLanguageTag(newBase)))
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
            var languageTag by remember { mutableStateOf(AppLocale.readLanguageTag(this)) }
            val localizedContext = remember(languageTag) {
                AppLocale.wrap(this, languageTag)
            }
            CompositionLocalProvider(LocalContext provides localizedContext) {
                HomeDecorApp(
                    viewModel = viewModel,
                    currentLanguageTag = languageTag,
                    onLanguageSelected = { selectedLanguageTag ->
                        languageTag = AppLocale.saveLanguageTag(this, selectedLanguageTag)
                    },
                )
            }
        }
    }
}
