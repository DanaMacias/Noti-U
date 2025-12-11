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

class AgregarPendienteViewModel : ViewModel() {

    private val pendientesRepo = PendientesRepository()
    private val materiaRepo = MateriaRepository()

    // Estados
    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    private val _materias = MutableStateFlow<List<Materia>>(emptyList())
    val materias: StateFlow<List<Materia>> = _materias

    init {
        cargarMaterias()
    }

    private fun cargarMaterias() {
        viewModelScope.launch {
            val listaMaterias = materiaRepo.obtenerMaterias()
            _materias.value = listaMaterias
        }
    }

    // Método para crear uno NUEVO
    fun guardar(titulo: String, descripcion: String, materiaId: String, fecha: String) {
        viewModelScope.launch {
            val nuevoPendiente = Pendientes(
                idPendientes = "", // ID vacío para crear nuevo
                titulo = titulo,
                descripcion = descripcion,
                materiaIdMateria = materiaId,
                fecha = fecha,
                estado = false
            )

            val resultado = pendientesRepo.guardarPendiente(nuevoPendiente)
            if (resultado.isSuccess) {
                _guardadoExitoso.value = true
            }
        }
    }

    // --- NUEVO MÉTODO: Actualizar uno EXISTENTE ---
    fun actualizarPendiente(id: String, titulo: String, descripcion: String, materiaId: String, fecha: String) {
        viewModelScope.launch {
            // Creamos el objeto usando el ID que recibimos
            val pendienteEditado = Pendientes(
                idPendientes = id, // Importante: Usamos el ID existente
                titulo = titulo,
                descripcion = descripcion,
                materiaIdMateria = materiaId,
                fecha = fecha,
                estado = false // Por defecto mantenemos false, o podríamos buscar el estado anterior si fuera necesario
            )

            // El repo detectará que tiene ID y hará update en lugar de push
            val resultado = pendientesRepo.guardarPendiente(pendienteEditado)
            if (resultado.isSuccess) {
                _guardadoExitoso.value = true
            }
        }
    }

    // --- NUEVO MÉTODO: Obtener datos para llenar el formulario en modo edición ---
    fun obtenerPendientePorId(id: String, onResult: (Pendientes?) -> Unit) {
        viewModelScope.launch {
            val pendiente = pendientesRepo.obtenerPendientePorId(id)
            onResult(pendiente)
        }
    }

    fun reiniciarEstado() {
        _guardadoExitoso.value = false
    }
}