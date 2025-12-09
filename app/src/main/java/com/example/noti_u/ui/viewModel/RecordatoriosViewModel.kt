package com.example.noti_u.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Recordatorios
import com.example.noti_u.data.repository.RecordatoriosRepository
import com.example.noti_u.utils.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RecordatoriosViewModel : ViewModel() {

    private val repository = RecordatoriosRepository()
    val recordatorios = mutableStateListOf<Recordatorios>()

    // Para guardar los recordatorios ya notificados
    private val recordatoriosNotificados = mutableSetOf<String>()
    private var checkJob: Job? = null

    fun cargarRecordatorios(userId: String) {
        repository.getRecordatoriosListener(userId) { lista ->
            recordatorios.clear()
            recordatorios.addAll(lista)
        }
    }

    // Iniciar verificación en tiempo real
    fun iniciarVerificacionNotificaciones(context: Context, userId: String) {
        checkJob?.cancel()
        checkJob = viewModelScope.launch {
            while (isActive) {
                verificarYNotificar(context)
                delay(10000) // Verificar cada 10 segundos
            }
        }
    }

    // Detener verificación
    fun detenerVerificacionNotificaciones() {
        checkJob?.cancel()
    }

    private fun verificarYNotificar(context: Context) {
        val ahora = System.currentTimeMillis()

        recordatorios.forEach { recordatorio ->
            if (recordatorio.notificar && !recordatoriosNotificados.contains(recordatorio.id)) {
                try {
                    val tiempoRecordatorio = parseDateTimeToMillis(recordatorio.fecha, recordatorio.hora)

                    // Si la hora del recordatorio ya pasó y fue hace menos de 1 minuto
                    if (tiempoRecordatorio in (ahora - 60000)..ahora) {
                        NotificationHelper.showNotification(
                            context,
                            recordatorio.nombre,
                            recordatorio.descripcion,
                            recordatorio.id
                        )
                        recordatoriosNotificados.add(recordatorio.id)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun parseDateTimeToMillis(fecha: String, hora: String): Long {
        val calendar = Calendar.getInstance()

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

        return calendar.timeInMillis
    }

    fun agregarRecordatorio(
        context: Context,
        fecha: String,
        hora: String,
        nombre: String,
        descripcion: String,
        notificar: Boolean,
        userId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val recordatorio = Recordatorios(
                    id = "",
                    fecha = fecha,
                    hora = hora,
                    nombre = nombre,
                    descripcion = descripcion,
                    notificar = notificar
                )
                repository.guardarRecordatorio(userId, recordatorio)

                if (notificar) {
                    NotificationHelper.scheduleNotification(
                        context,
                        recordatorio.id,
                        nombre,
                        descripcion,
                        fecha,
                        hora
                    )
                }

                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun editarRecordatorio(
        context: Context,
        recordatorioId: String,
        fecha: String,
        hora: String,
        nombre: String,
        descripcion: String,
        notificar: Boolean,
        userId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Cancelar notificación previa
                NotificationHelper.cancelNotification(context, recordatorioId)

                val recordatorio = Recordatorios(
                    id = recordatorioId,
                    fecha = fecha,
                    hora = hora,
                    nombre = nombre,
                    descripcion = descripcion,
                    notificar = notificar
                )
                repository.editarRecordatorio(userId, recordatorio)

                if (notificar) {
                    NotificationHelper.scheduleNotification(
                        context,
                        recordatorioId,
                        nombre,
                        descripcion,
                        fecha,
                        hora
                    )
                }

                // Remover de notificados para que pueda volver a notificar
                recordatoriosNotificados.remove(recordatorioId)

                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun eliminarRecordatorio(
        context: Context,
        userId: String,
        recordatorioId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                NotificationHelper.cancelNotification(context, recordatorioId)
                repository.eliminarRecordatorio(userId, recordatorioId)
                recordatoriosNotificados.remove(recordatorioId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun consultarRecordatorio(userId: String, recordatorioId: String): Recordatorios? {
        return repository.consultarRecordatorio(userId, recordatorioId)
    }

    override fun onCleared() {
        super.onCleared()
        detenerVerificacionNotificaciones()
    }
}