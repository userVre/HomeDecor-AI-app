package com.ismail.homedecorai

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeDecorApplication : Application() {
    lateinit var services: NativeServices
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        services = NativeServices()

        initRevenueCatAsync()
    }

    private fun initRevenueCatAsync() {
        if (BuildConfig.REVENUECAT_ANDROID_API_KEY.isBlank()) return
        appScope.launch(Dispatchers.IO) {
            if (BuildConfig.DEBUG) {
                Purchases.logLevel = LogLevel.DEBUG
            }
            Purchases.configure(
                PurchasesConfiguration.Builder(this@HomeDecorApplication, BuildConfig.REVENUECAT_ANDROID_API_KEY).build()
            )
        }
    }
}
