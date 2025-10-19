package com.example.sombriyakotlin.ui.account.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor (
    private val userUseCases: UserUseCases
) : ViewModel() {

    sealed class SignUpState {
        object Idle : SignUpState()
        object Loading : SignUpState()
        data class Success(val user: User) : SignUpState()
        data class Error(val message: String) : SignUpState()
    }
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    // 1. AÑADIMOS EL PARÁMETRO 'pass'
    fun registerUser(name: String, email: String, pass: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val user = CreateUser( name = name, email = email, password = pass)
                
                val createdUser = userUseCases.createUserUseCase.invoke(user)
                Log.d("SignUpViewModel", "Usuario registrado: $createdUser")
                _signUpState.value = SignUpState.Success(createdUser)
            } catch (e: Exception) {
                // 3. AÑADIMOS UN LOG PARA VER EL ERROR EN LOGCAT
                Log.e("SignUpViewModel", "Error al registrar: ${e.message}", e)
                _signUpState.value = SignUpState.Error("Error al registrar el usuario: ${e.message}")
            }        }
    }
}
