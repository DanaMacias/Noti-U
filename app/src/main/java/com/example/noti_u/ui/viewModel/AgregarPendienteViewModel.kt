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
// Ya no necesitamos Date ni SimpleDateFormat aquí porque la fecha viene de la Vista

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

    // --- CAMBIO AQUÍ: Agregamos el parámetro 'fecha' ---
    fun guardar(titulo: String, descripcion: String, materiaId: String, fecha: String) {
        viewModelScope.launch {

            // Eliminamos la línea que calculaba la fecha automática:
            // val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

            val nuevoPendiente = Pendientes(
                titulo = titulo,
                descripcion = descripcion,
                materiaIdMateria = materiaId,
                fecha = fecha, // --- CAMBIO AQUÍ: Usamos la fecha que recibimos del calendario ---
                estado = false
            )

            val resultado = pendientesRepo.guardarPendiente(nuevoPendiente)
            if (resultado.isSuccess) {
                _guardadoExitoso.value = true
            }
        }
    }

    fun reiniciarEstado() {
        _guardadoExitoso.value = false
    }
}