package com.example.noti_u.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Recordatorios
import com.example.noti_u.data.repository.RecordatoriosRepository
import kotlinx.coroutines.launch

class RecordatoriosViewModel : ViewModel() {

    private val repository = RecordatoriosRepository()
    var recordatorios = mutableStateListOf<Recordatorios>()
        private set

    fun cargarRecordatorios(userId: String) {
        repository.getRecordatoriosListener(userId) { listaRecordatorios ->
            recordatorios.clear()
            recordatorios.addAll(listaRecordatorios)
        }
    }

    fun agregarRecordatorio(
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
                    fecha = fecha,
                    hora = hora,
                    nombre = nombre,
                    descripcion = descripcion,
                    notificar = notificar,
                    userId = userId
                )
                repository.guardarRecordatorio(userId, recordatorio)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun editarRecordatorio(
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
                val recordatorio = Recordatorios(
                    id = recordatorioId,
                    fecha = fecha,
                    hora = hora,
                    nombre = nombre,
                    descripcion = descripcion,
                    notificar = notificar,
                    userId = userId
                )
                repository.editarRecordatorio(userId, recordatorio)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun consultarRecordatorio(userId: String, recordatorioId: String): Recordatorios? {
        return try {
            repository.consultarRecordatorio(userId, recordatorioId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun eliminarRecordatorio(userId: String, recordatorioId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarRecordatorio(userId, recordatorioId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}