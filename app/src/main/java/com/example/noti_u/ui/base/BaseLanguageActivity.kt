package com.example.noti_u.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.noti_u.utils.LocaleHelper
import java.util.Locale

open class BaseLanguageActivity : ComponentActivity() {
    private var currentLanguage = ""

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        currentLanguage = prefs.getString("language", "es") ?: "es"
        val context = LocaleHelper.setLocale(newBase, currentLanguage)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Sin recreate()
    }

    override fun onResume() {
        super.onResume()
        checkLanguageChange()
    }

    private fun checkLanguageChange() {
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val newLanguage = prefs.getString("language", "es") ?: "es"
        if (currentLanguage != newLanguage) {
            currentLanguage = newLanguage
            recreate() // Solo aquí se llama recreate()
        }
    }
}