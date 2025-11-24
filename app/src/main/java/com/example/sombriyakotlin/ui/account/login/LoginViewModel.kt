package com.example.sombriyakotlin.ui.account.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.GoogleLogIn
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val rentalUseCases: RentalUseCases,
) : ViewModel() {

    // Clase sellada para manejar los estados de la UI
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun loginUser(email: String,password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val credentials = LogInUser(email, password)
                val loggedInUser = userUseCases.logInUserUseCases(credentials)
                _loginState.value = LoginState.Success(loggedInUser)
                Log.e("LoginViewModel", "bien: ${loggedInUser.id}")
                upDateRentalLocal()
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en el login: ${e.message}", e)
                _loginState.value = LoginState.Error("Error en el login: Compruebe sus credenciales")
            }
        }
    }

    fun googleLoginUser(id: String?) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                if (id is String) {
                    val credentials = GoogleLogIn(id)
                    val loggedInUser = userUseCases.googleLogInUserUseCases(credentials)
                    _loginState.value = LoginState.Success(loggedInUser)
                    Log.d("LoginViewModel", "bien: ${loggedInUser.id}")
                    upDateRentalLocal()
                }else {
                    Log.w("LoginViewModel", "Credential is not of type Google ID!")
                    _loginState.value = LoginState.Error("Error inseperado en el login")

                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en el login: ${e.message}", e)
                _loginState.value = LoginState.Error("Error en el login: ${e.message}")
            }
        }
    }


    private suspend fun upDateRentalLocal() {
        val user = userUseCases.getUserUseCase().first()

        if (user != null) {
            Log.d("RENTALSLOGIN", "Cargando rentas del usuario...")
            val rentals = rentalUseCases.getRentalsUserUseCase.invoke(user.id, status = "ongoing")
            Log.d("RENTALSLOGIN", "Rentas obtenidas: ${rentals.size}")

            if (rentals.isNotEmpty()) {
                Log.d("RENTALSLOGIN", "Renta encontrada: ${rentals[0]}")
                rentalUseCases.setCurrentRentalUseCase.invoke(rentals[0])
            }

            Log.d("RENTALSLOGIN", "Se ha actualizado la renta con exito")
        }
    }

}
