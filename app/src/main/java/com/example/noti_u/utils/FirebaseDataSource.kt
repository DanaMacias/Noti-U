package com.example.noti_u.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseDataSource {

    // auth
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // realtime database
    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
}
