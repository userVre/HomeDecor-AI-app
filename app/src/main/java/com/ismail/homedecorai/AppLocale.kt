package com.ismail.homedecorai

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

data class AppLanguageOption(
    val tag: String,
    val label: String,
)

object AppLocale {
    private const val PREFS_NAME = "home_decor_locale"
    private const val PREF_LANGUAGE_TAG = "language_tag"
    private const val DEFAULT_LANGUAGE_TAG = "fr"

    val supportedLanguages = listOf(
        AppLanguageOption(tag = "fr", label = "Français"),
        AppLanguageOption(tag = "en", label = "English"),
        AppLanguageOption(tag = "ar", label = "Arabic"),
    )

    fun readLanguageTag(context: Context): String {
        val saved = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PREF_LANGUAGE_TAG, null)
        return normalizeLanguageTag(saved)
    }

    fun saveLanguageTag(context: Context, languageTag: String): String {
        val normalized = normalizeLanguageTag(languageTag)
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_LANGUAGE_TAG, normalized)
            .apply()
        return normalized
    }

    fun labelFor(languageTag: String): String {
        val normalized = normalizeLanguageTag(languageTag)
        return supportedLanguages.firstOrNull { it.tag == normalized }?.label
            ?: supportedLanguages.first().label
    }

    fun wrap(context: Context, languageTag: String): Context {
        val normalized = normalizeLanguageTag(languageTag)
        val locale = Locale.forLanguageTag(normalized)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun normalizeLanguageTag(languageTag: String?): String {
        val language = languageTag
            ?.takeIf { it.isNotBlank() }
            ?.let { Locale.forLanguageTag(it).language }
            ?: DEFAULT_LANGUAGE_TAG
        return supportedLanguages.firstOrNull { it.tag == language }?.tag
            ?: DEFAULT_LANGUAGE_TAG
    }
}
