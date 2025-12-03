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
            _materias.value = repository.obtenerMaterias()
        }
    }

    fun guardarMateria(materia: Materia) {
        viewModelScope.launch {
            val resultado = repository.guardarMateria(materia)
            _guardadoExitoso.value = resultado.isSuccess

            if (resultado.isSuccess) {
                cargarMaterias()
            }
        }
    }

    fun resetGuardado() {
        _guardadoExitoso.value = false
    }
}
