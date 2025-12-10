package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.repository.MateriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MateriaViewModel(
    private val repository: MateriaRepository = MateriaRepository()
) : ViewModel() {

    private val _materias = MutableStateFlow<List<Materia>>(emptyList())
    val materias: StateFlow<List<Materia>> = _materias

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso

    fun cargarMaterias() {
        viewModelScope.launch {
            // 1. FIRST Check if period is over and delete if necessary
            repository.verificarVencimientoPeriodo()

            // 2. THEN load the list (it will be empty if deleted above)
            _materias.value = repository.obtenerMaterias()
        }
    }

    // ... (Keep guardarMateria, eliminarMateria, obtenerMateriaPorId, resetGuardado as they were) ...
    fun guardarMateria(materia: Materia) {
        viewModelScope.launch {
            val result = repository.guardarMateria(materia)
            _guardadoExitoso.value = result.isSuccess
            if (result.isSuccess) cargarMaterias()
        }
    }

    fun eliminarMateria(id: String) {
        viewModelScope.launch {
            if(repository.eliminarMateria(id)) cargarMaterias()
        }
    }

    fun obtenerMateriaPorId(id: String, onResult: (Materia?) -> Unit) {
        viewModelScope.launch { onResult(repository.obtenerMateriaPorId(id)) }
    }

    fun resetGuardado() { _guardadoExitoso.value = false }
}