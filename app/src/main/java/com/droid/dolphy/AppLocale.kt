package com.droid.dolphy

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale






object AppLocale {
    const val DEFAULT_LANGUAGE = "ar"

    fun normalize(language: String?): String = when (language) {
        "en" -> "en"
        "ru" -> "ru"
        else -> "ar"
    }

    fun wrap(context: Context): Context {
        val prefs = context.getSharedPreferences("DolphyPrefs", Context.MODE_PRIVATE)
        val language = prefs.getString("app_language", DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        val locale = Locale.forLanguageTag(normalize(language))
        Locale.setDefault(locale)
        Locale.setDefault(Locale.Category.DISPLAY, locale)

        val config = Configuration(context.resources.configuration)
        config.setLocales(android.os.LocaleList(locale))

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }
}

