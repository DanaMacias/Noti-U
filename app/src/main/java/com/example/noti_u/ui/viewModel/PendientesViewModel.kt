package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.data.repository.MateriaRepository
import com.example.noti_u.data.repository.PendientesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PendientesViewModel : ViewModel() {
    private val pendientesRepo = PendientesRepository()
    private val materiaRepo = MateriaRepository()

    private val _pendientes = MutableStateFlow<List<Pendientes>>(emptyList())
    val pendientes: StateFlow<List<Pendientes>> = _pendientes

    // Mapa para buscar rápido: ID -> Materia
    private val _materiasMap = MutableStateFlow<Map<String, Materia>>(emptyMap())
    val materiasMap: StateFlow<Map<String, Materia>> = _materiasMap

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            // 1. Cargar materias (usando tu repo) y convertir a Mapa
            val listaMaterias = materiaRepo.obtenerMaterias()
            _materiasMap.value = listaMaterias.associateBy { it.id }

            // 2. Escuchar pendientes en tiempo real
            pendientesRepo.obtenerPendientes().collect { lista ->
                _pendientes.value = lista
            }
        }
    }

    fun cambiarEstadoPendiente(pendiente: Pendientes, nuevoEstado: Boolean) {
        viewModelScope.launch {
            val pendienteActualizado = pendiente.copy(estado = nuevoEstado)
            pendientesRepo.guardarPendiente(pendienteActualizado)
        }
    }

    // Función para borrar
    fun eliminarPendiente(id: String) {
        viewModelScope.launch {
            pendientesRepo.eliminarPendiente(id)
        }
    }
}