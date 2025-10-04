package com.example.sombriyakotlin.ui.account.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor (
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    sealed class SignUpState {
        object Idle : SignUpState()
        object Loading : SignUpState()
        data class Success(val user: User) : SignUpState()
        data class Error(val message: String) : SignUpState()
    }
    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun registerUser(name: String, email: String, ) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val user = User(id = 0, name = name, email = email)
                val createdUser = createUserUseCase.invoke(user)
                _signUpState.value = SignUpState.Success(createdUser)
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error("Error al registrar el usuario: ${e.message}")
            }
        }
    }


}