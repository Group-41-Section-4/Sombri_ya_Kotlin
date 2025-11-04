package com.example.sombriyakotlin.ui.account.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.GoogleLogIn
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val rentalUseCases: RentalUseCases
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
                _loginState.value = LoginState.Error("Error en el login: ${e.message}")
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
                    Log.e("LoginViewModel", "bien: ${loggedInUser.id}")
                    upDateRentalLocal()
                }else {
                    Log.w("LoginViewModel", "Credential is not of type Google ID!")
                    _loginState.value = LoginState.Error("Error inseperado en el login 2000")

                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en el login: ${e.message}", e)
                _loginState.value = LoginState.Error("Error en el login: ${e.message}")
            }
        }
    }

    fun handleSignIn(id: String?, auth: FirebaseAuth) {
        // Check if credential is of type Google ID
        viewModelScope.launch {
//            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                // Create Google ID Token
//                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//                Log.d("LoginViewModel", "firebase googleIdTokenCredential: $googleIdTokenCredential")

                // Sign in to Firebase with using the token
            if (id is String) {
                firebaseAuthWithGoogle(id, auth)
                upDateRentalLocal()

            } else {
                Log.w("LoginViewModel", "Credential is not of type Google ID!")
                _loginState.value = LoginState.Error("Credential is not of type Google ID!")

            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String, auth:  FirebaseAuth) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginViewModel", "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("LoginViewModel", "User: $user")
                    _loginState.value = LoginState.Success(User(user?.uid ?: "", user?.displayName ?: "", user?.email ?: "",))
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w("LoginViewModel", "signInWithCredential:failure", task.exception)
                    _loginState.value = LoginState.Error("Error en el login: ${task.exception}")

//                    updateUI(null)
                }
            }
    }

    private suspend fun upDateRentalLocal() {
        val user = userUseCases.getUserUseCase().first()

        if (user != null) {
            Log.d("RENTALSLOGIN", "Cargando rentas del usuario...")
            val rentals = rentalUseCases.getRentalsUserUseCase.invoke(user.id, status = "ongoing")
            Log.d("RENTALSLOGIN", "Rentas obtenidas: ${rentals.size}")
            Log.d("RENTALSLOGIN","RETNAAAAAAAAA ${rentals[0]}")
            rentalUseCases.setCurrentRentalUseCase.invoke(rentals[0])
            Log.d("RENTALSLOGIN", "Se ha actualizado la renta con exito")
        }
    }

}
