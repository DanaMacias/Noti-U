package com.example.noti_u

import android.content.Intent
import android.os.Bundle
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.screens.LoginActivity
import com.google.firebase.FirebaseApp

class MainActivity : BaseLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this) // ANTES de super.onCreate
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}