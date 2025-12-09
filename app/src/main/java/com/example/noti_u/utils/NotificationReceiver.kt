package com.example.noti_u.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "Recordatorio"
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val recordatorioId = intent.getStringExtra("recordatorioId") ?: ""

        NotificationHelper.showNotification(context, titulo, descripcion, recordatorioId)
    }
}