package com.girendi.tobamembers.persentation.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserUseCase: RegisterUserUseCase) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _items = MutableLiveData<List<String>>()
    val items: LiveData<List<String>> = _items

    private val _isUsernameExists = MutableLiveData<Boolean>()
    val isUsernameExists: LiveData<Boolean> = _isUsernameExists

    private val _isEmailExists = MutableLiveData<Boolean>()
    val isEmailExists: LiveData<Boolean> = _isEmailExists

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    init {
        _items.value = listOf("Admin", "User")
    }

    fun registerUser(username: String, email: String, role: String, password: String) {
        val newUser = UserEntity(
            username = username,
            email = email,
            role = role,
            password = password
        )
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = registerUserUseCase.registerUser(newUser)) {
                is Result.Success -> {
                    if (result.data) {
                        _success.value = true
                        _uiState.value = UiState.Success
                    } else {
                        _success.value = false
                        _uiState.value = UiState.Error("Data cannot be saved!")
                    }
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "An unknown error occurred")
                }
                is Result.Loading -> {
                    _uiState.value = UiState.Loading
                }
            }
        }
    }

    fun checkUsername(username: String) {
        val invalidUsername = username.length < 6
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = registerUserUseCase.countUsersWithUsername(username)) {
                is Result.Success -> {
                    _isUsernameExists.value = result.data || invalidUsername
                    _uiState.value = UiState.Success
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "An unknown error occurred")
                }
                is Result.Loading -> {
                    _uiState.value = UiState.Loading
                }
            }
        }
    }

    fun checkEmail(email: String) {
        val invalidEmail = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = registerUserUseCase.countUsersWithEmail(email)) {
                is Result.Success -> {
                    _isEmailExists.value = result.data || invalidEmail
                    _uiState.value = UiState.Success
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "An unknown error occurred")
                }
                is Result.Loading -> {
                    _uiState.value = UiState.Loading
                }
            }
        }
    }
}