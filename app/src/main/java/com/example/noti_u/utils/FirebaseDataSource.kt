package com.example.noti_u.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseDataSource {

    // aut
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // db
    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
}