package com.example.sombriyakotlin.ui.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    sealed class ProfileState {
        object Idle : ProfileState()
        object Loading : ProfileState()
        data class Success(val user: User) : ProfileState()
        data class Error(val message: String) : ProfileState()
    }

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try
            {/*
            Falta que exista updateUser.name
                val updatedUser = userUseCases.updateUserNameUseCase(newName)
                _profileState.value = ProfileState.Success(updatedUser)

                Log.d("ProfileViewModel", "Nombre actualizado: ${updatedUser.name}")

                */
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error actualizando nombre: ${e.message}", e)
                _profileState.value = ProfileState.Error("Error actualizando nombre")
            }
        }
    }
}
