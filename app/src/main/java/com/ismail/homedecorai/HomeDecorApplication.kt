package com.ismail.homedecorai

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration

class HomeDecorApplication : Application() {
    lateinit var services: NativeServices
        private set

    override fun onCreate() {
        super.onCreate()
        services = NativeServices()

        if (BuildConfig.REVENUECAT_ANDROID_API_KEY.isNotBlank()) {
            Purchases.logLevel = LogLevel.DEBUG
            Purchases.configure(
                PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_ANDROID_API_KEY).build()
            )
        }
    }
}
