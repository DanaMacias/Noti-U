package com.example.noti_u.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.noti_u.utils.LocaleHelper

class LanguageViewModel : ViewModel() {

    fun cambiarIdioma(context: Context, idioma: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", idioma).apply()

        LocaleHelper.setLocale(context, idioma)
    }
}
