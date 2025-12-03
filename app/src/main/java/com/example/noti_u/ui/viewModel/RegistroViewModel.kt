package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.User
import com.example.noti_u.data.repository.AuthRepository
import com.example.noti_u.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val authRepo: AuthRepository = AuthRepository(),
    private val userRepo: UserRepository = UserRepository()
) : ViewModel() {

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
        if (nombre.isBlank() || correo.isBlank() || telefono.isBlank() ||
            area.isBlank() || institucion.isBlank() || password.isBlank()
        ) {
            _registerState.value = Result.failure(Exception("Debes llenar todos los campos"))
            return
        }

        viewModelScope.launch {
            // 1️⃣ Registrar en Firebase Auth
            val authResult = authRepo.register(correo, password)

            if (authResult.isFailure) {
                _registerState.value = Result.failure(authResult.exceptionOrNull()!!)
                return@launch
            }

            val uid = authResult.getOrNull()!!  // UID generado por Firebase

            // 2️⃣ Crear objeto User completo y guardarlo en Realtime Database
            val user = User(
                id = uid,
                nombre = nombre,
                correo = correo,
                telefono = telefono,
                area = area,
                institucion = institucion
            )

            val dbResult = userRepo.saveUser(user)

            _registerState.value = dbResult
        }
    }
}
