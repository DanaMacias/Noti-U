package com.example.noti_u.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Notas
import com.example.noti_u.data.repository.NotasRepository
import kotlinx.coroutines.launch

class NotasViewModel : ViewModel() {

    private val repository = NotasRepository()
    var notas = mutableStateListOf<Notas>()
        private set

    fun cargarNotas(userId: String) {
        repository.getNotasListener(userId) { listaNotas ->
            notas.clear()
            notas.addAll(listaNotas)
        }
    }

    fun agregarNota(titulo: String, descripcion: String, userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nota = Notas(
                    titulo = titulo,
                    descripcion = descripcion,
                    userId = userId
                )
                repository.guardarNota(userId, nota)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun editarNota(notaId: String, titulo: String, descripcion: String, userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val nota = Notas(
                    id = notaId,
                    titulo = titulo,
                    descripcion = descripcion,
                    userId = userId
                )
                repository.editarNota(userId, nota)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun consultarNota(userId: String, notaId: String): Notas? {
        return try {
            repository.consultarNota(userId, notaId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun eliminarNota(userId: String, notaId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.eliminarNota(userId, notaId)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}