package com.example.noti_u.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        return context.updateLocale(language)
    }

    fun applyLocale(activity: Activity, language: String) {
        activity.updateLocale(language)
    }

    private fun Context.updateLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = this.resources
        val config = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            return createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLayoutDirection(locale)
            }
            resources.updateConfiguration(config, resources.displayMetrics)
            return this
        }
    }
}