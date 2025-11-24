package com.example.noti_u.ui.base



import android.content.Context
import androidx.activity.ComponentActivity
import com.example.noti_u.utils.LocaleHelper

open class BaseLanguageActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val prefs = newBase?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs?.getString("language", "es") ?: "es"

        val updated = LocaleHelper.setLocale(newBase!!, lang)
        super.attachBaseContext(updated)
    }
}
