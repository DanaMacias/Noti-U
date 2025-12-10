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

    // MÉTODO CORREGIDO: Recibe email, password y el objeto User completo
    fun register(email: String, password: String, userData: User) {

        // Validación básica (los campos del periodo son opcionales si el usuario aceptó la alerta)
        if (userData.nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = Result.failure(Exception("Faltan campos obligatorios"))
            return
        }

        viewModelScope.launch {
            // 1. Registrar en Firebase Authentication
            val authResult = authRepo.register(email, password)

            if (authResult.isFailure) {
                _registerState.value = Result.failure(authResult.exceptionOrNull()!!)
                return@launch
            }

            val uid = authResult.getOrNull()!! // Obtener el UID generado

            // 2. Asignar el UID al objeto User y guardarlo en Database
            val userToSave = userData.copy(id = uid)

            val dbResult = userRepo.saveUser(userToSave)

            _registerState.value = dbResult
        }
    }
}