package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.User
import com.example.noti_u.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {

    private val _registerState = MutableStateFlow<Result<Unit>?>(null)
    val registerState: StateFlow<Result<Unit>?> get() = _registerState

    fun register(
        nombre: String,
        correo: String,
        telefono: String,
        area: String,
        institucion: String,
        password: String
    ) {
        if (nombre.isBlank() || correo.isBlank() || telefono.isBlank() || area.isBlank() || institucion.isBlank() || password.isBlank()) {
            _registerState.value = Result.failure(Exception("Debes llenar todos los campos"))
            return
        }

        viewModelScope.launch {
            val user = User(
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                area = area,
                institucion = institucion
            )
            val result = repo.register(user, password)
            _registerState.value = result
        }
    }
}
