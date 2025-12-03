package com.example.sombriyakotlin.ui.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import com.example.sombriyakotlin.ui.account.ProfileScreenViewModel.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    ) : ViewModel() {

    sealed class ProfileState {
        object Idle : ProfileState()
        object Loading : ProfileState()
        data class Success(val user: User) : ProfileState()
        data class Error(val message: String) : ProfileState()
    }
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val user = userUseCases.getUserUseCase().first()
                Log.d("ProfileViewModel", "User loaded: ${user?.name}")
                if (user != null) {
                    _profileState.value = ProfileState.Success(user)
                } else {
                    _profileState.value = ProfileState.Error("User not found")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading initial data: ${e.message}", e)
                _profileState.value = ProfileState.Error("Error loading data")
            }
        }
    }

}