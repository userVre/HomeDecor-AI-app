package com.ismail.homedecorai

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

data class AppLanguageOption(
    val tag: String,
    @StringRes val labelRes: Int,
)

object AppLocale {
    private const val PREFS_NAME = "home_decor_locale"
    private const val PREF_LANGUAGE_TAG = "language_tag"
    private const val PREF_FIRST_LAUNCH_DONE = "first_launch_language_done"
    const val SYSTEM_LANGUAGE_TAG = "system"
    private const val DEFAULT_LANGUAGE_TAG = "en-US"

    val supportedLanguages = listOf(
        AppLanguageOption(tag = "en-US", labelRes = R.string.language_name_en_us),
        AppLanguageOption(tag = "ar", labelRes = R.string.language_name_ar),
        AppLanguageOption(tag = "sv", labelRes = R.string.language_name_sv),
        AppLanguageOption(tag = "de", labelRes = R.string.language_name_de),
        AppLanguageOption(tag = "it", labelRes = R.string.language_name_it),
        AppLanguageOption(tag = "ja", labelRes = R.string.language_name_ja),
        AppLanguageOption(tag = "ko", labelRes = R.string.language_name_ko),
        AppLanguageOption(tag = "fr", labelRes = R.string.language_name_fr),
        AppLanguageOption(tag = "pt", labelRes = R.string.language_name_pt),
        AppLanguageOption(tag = "pt-BR", labelRes = R.string.language_name_pt_br),
        AppLanguageOption(tag = "es", labelRes = R.string.language_name_es),
        AppLanguageOption(tag = "es-MX", labelRes = R.string.language_name_es_mx),
        AppLanguageOption(tag = "ru", labelRes = R.string.language_name_ru),
        AppLanguageOption(tag = "zh-Hans", labelRes = R.string.language_name_zh_hans),
        AppLanguageOption(tag = "zh-Hant", labelRes = R.string.language_name_zh_hant),
        AppLanguageOption(tag = "vi", labelRes = R.string.language_name_vi),
    )

    private val countryToLanguage = mapOf(
        "US" to "en-US", "GB" to "en-US", "AU" to "en-US", "NZ" to "en-US",
        "IE" to "en-US", "SG" to "en-US", "IN" to "en-US", "ZA" to "en-US",
        "FR" to "fr", "BE" to "fr", "CH" to "fr", "MC" to "fr", "LU" to "fr",
        "MA" to "ar", "TN" to "ar", "DZ" to "ar", "EG" to "ar",
        "SA" to "ar", "AE" to "ar", "QA" to "ar", "KW" to "ar",
        "BH" to "ar", "OM" to "ar", "JO" to "ar", "LB" to "ar",
        "IQ" to "ar", "SY" to "ar", "YE" to "ar", "LY" to "ar", "SD" to "ar",
        "DE" to "de", "AT" to "de", "LI" to "de",
        "IT" to "it", "SM" to "it", "VA" to "it",
        "ES" to "es", "AD" to "es",
        "MX" to "es-MX", "CO" to "es-MX", "AR" to "es-MX", "CL" to "es-MX",
        "PE" to "es-MX", "VE" to "es-MX", "EC" to "es-MX", "BO" to "es-MX",
        "PY" to "es-MX", "UY" to "es-MX", "CR" to "es-MX", "PA" to "es-MX",
        "GT" to "es-MX", "HN" to "es-MX", "SV" to "es-MX", "NI" to "es-MX",
        "DO" to "es-MX", "CU" to "es-MX", "PR" to "es-MX",
        "JP" to "ja",
        "KR" to "ko",
        "PT" to "pt", "MO" to "pt",
        "BR" to "pt-BR",
        "RU" to "ru", "BY" to "ru",
        "CN" to "zh-Hans",
        "TW" to "zh-Hant", "HK" to "zh-Hant",
        "SE" to "sv", "FI" to "sv",
        "VN" to "vi",
    )

    fun detectLanguageFromSystem(context: Context): String {
        val configuration = context.resources.configuration
        val deviceLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
        val country = deviceLocale.country
        if (country.isNotBlank()) {
            countryToLanguage[country.uppercase(Locale.ROOT)]?.let { return it }
        }
        return normalizeManualLanguageTag(deviceLocale.toLanguageTag())
    }

    private fun autoDetectOnFirstLaunch(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(PREF_FIRST_LAUNCH_DONE, false)) {
            return SYSTEM_LANGUAGE_TAG
        }
        prefs.edit().putBoolean(PREF_FIRST_LAUNCH_DONE, true).apply()
        val detected = detectLanguageFromSystem(context)
        prefs.edit().putString(PREF_LANGUAGE_TAG, detected).apply()
        return detected
    }

    fun readLanguageTag(context: Context): String {
        val saved = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PREF_LANGUAGE_TAG, null)
        if (saved != null) {
            return normalizePreferenceTag(saved)
        }
        return autoDetectOnFirstLaunch(context)
    }

    fun saveLanguageTag(context: Context, languageTag: String): String {
        val normalized = normalizePreferenceTag(languageTag)
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_LANGUAGE_TAG, normalized)
            .apply()
        return normalized
    }

    fun labelFor(context: Context, languageTag: String): String {
        val normalized = normalizePreferenceTag(languageTag)
        if (normalized == SYSTEM_LANGUAGE_TAG) {
            return context.getString(R.string.language_system_default)
        }
        return supportedLanguages.firstOrNull { it.tag == normalized }?.let { context.getString(it.labelRes) }
            ?: context.getString(R.string.language_name_en_us)
    }

    fun wrap(context: Context, languageTag: String): Context {
        val locale = Locale.forLanguageTag(resolveLanguageTag(context, languageTag))
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun layoutDirectionFor(context: Context, languageTag: String): LayoutDirection {
        val locale = Locale.forLanguageTag(resolveLanguageTag(context, languageTag))
        return if (locale.language == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
    }

    private fun resolveLanguageTag(context: Context, preferenceTag: String?): String {
        val normalized = normalizePreferenceTag(preferenceTag)
        if (normalized != SYSTEM_LANGUAGE_TAG) {
            return normalized
        }

        val configuration = context.resources.configuration
        val deviceLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
        return normalizeManualLanguageTag(deviceLocale.toLanguageTag())
    }

    private fun normalizePreferenceTag(languageTag: String?): String {
        val normalized = languageTag?.trim()?.takeIf { it.isNotBlank() } ?: SYSTEM_LANGUAGE_TAG
        if (normalized.equals(SYSTEM_LANGUAGE_TAG, ignoreCase = true) || normalized.equals("auto", ignoreCase = true)) {
            return SYSTEM_LANGUAGE_TAG
        }
        return normalizeManualLanguageTag(normalized)
    }

    private fun normalizeManualLanguageTag(languageTag: String?): String {
        val raw = languageTag?.trim()?.replace('_', '-')?.takeIf { it.isNotBlank() }
            ?: DEFAULT_LANGUAGE_TAG
        val normalized = raw.lowercase(Locale.ROOT)
        return when {
            normalized == "en" || normalized.startsWith("en-") -> "en-US"
            normalized == "ar" || normalized.startsWith("ar-") -> "ar"
            normalized == "sv" || normalized.startsWith("sv-") -> "sv"
            normalized == "de" || normalized.startsWith("de-") -> "de"
            normalized == "it" || normalized.startsWith("it-") -> "it"
            normalized == "ja" || normalized.startsWith("ja-") -> "ja"
            normalized == "ko" || normalized.startsWith("ko-") -> "ko"
            normalized == "fr" || normalized.startsWith("fr-") -> "fr"
            normalized == "pt-br" || normalized.startsWith("pt-br") -> "pt-BR"
            normalized == "pt" || normalized.startsWith("pt-") -> "pt"
            normalized == "es-mx" || normalized.startsWith("es-mx") -> "es-MX"
            normalized == "es" || normalized.startsWith("es-") -> "es"
            normalized == "ru" || normalized.startsWith("ru-") -> "ru"
            normalized == "vi" || normalized.startsWith("vi-") -> "vi"
            normalized == "zh-hant" || normalized.contains("hant") ||
                normalized.startsWith("zh-tw") || normalized.startsWith("zh-hk") || normalized.startsWith("zh-mo") -> "zh-Hant"
            normalized == "zh-hans" || normalized.contains("hans") ||
                normalized.startsWith("zh-cn") || normalized.startsWith("zh-sg") || normalized.startsWith("zh-") -> "zh-Hans"
            else -> DEFAULT_LANGUAGE_TAG
        }
    }
}
