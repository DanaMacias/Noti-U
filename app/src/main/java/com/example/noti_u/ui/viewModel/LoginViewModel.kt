package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState: StateFlow<Result<Boolean>?> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(email, password)
            _loginState.value = result.map { true }
        }
    }
}
