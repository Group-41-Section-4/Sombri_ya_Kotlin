package com.example.sombriyakotlin.ui.account.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _biometricUserState = MutableStateFlow<User?>(null)
    val biometricUserState: StateFlow<User?> = _biometricUserState

    init {
        viewModelScope.launch {
            val user = userUseCases.getUserUseCase().firstOrNull()
            //if (user != null && user.biometricEnabled == "true") {
            if (user != null) {
                _biometricUserState.value = user
            }
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun loginUser(userId: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val loggedInUser = userUseCases.refreshUserUseCase(userId)
                _loginState.value = LoginState.Success(loggedInUser)
                Log.e("LoginViewModel", "bien: ${loggedInUser.id}")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en el login: ${e.message}", e)
                _loginState.value = LoginState.Error("Error en el login: ${e.message}")
            }
        }
    }
}
