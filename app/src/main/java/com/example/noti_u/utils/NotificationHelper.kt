package com.example.noti_u.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.noti_u.R
import com.example.noti_u.MainActivity
import java.util.*

object NotificationHelper {

    private const val CHANNEL_ID = "recordatorios_channel"
    private const val CHANNEL_NAME = "Recordatorios"
    private const val CHANNEL_DESCRIPTION = "Notificaciones de recordatorios"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(
        context: Context,
        recordatorioId: String,
        titulo: String,
        descripcion: String,
        fecha: String,
        hora: String
    ) {
        try {
            val calendar = parseDateTime(fecha, hora)

            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                return
            }

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = "com.example.noti_u.NOTIFICATION_ACTION"
                putExtra("recordatorioId", recordatorioId)
                putExtra("titulo", titulo)
                putExtra("descripcion", descripcion)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                recordatorioId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelNotification(context: Context, recordatorioId: String) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            recordatorioId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun showNotification(context: Context, titulo: String, descripcion: String, recordatorioId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(titulo)
            .setContentText(descripcion)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(recordatorioId.hashCode(), notification)
    }

    private fun parseDateTime(fecha: String, hora: String): Calendar {
        val calendar = Calendar.getInstance()

        try {
            val fechaParts = fecha.split("/")
            val day = fechaParts[0].toInt()
            val month = fechaParts[1].toInt() - 1
            val year = fechaParts[2].toInt()

            val horaParts = hora.replace("AM", "").replace("PM", "").trim().split(":")
            var hour = horaParts[0].toInt()
            val minute = horaParts[1].trim().toInt()

            if (hora.contains("PM") && hour != 12) {
                hour += 12
            } else if (hora.contains("AM") && hour == 12) {
                hour = 0
            }

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return calendar
    }
}