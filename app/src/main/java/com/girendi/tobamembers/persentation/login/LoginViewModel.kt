package com.girendi.tobamembers.persentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.launch


class LoginViewModel(private val loginUserUseCase: LoginUserUseCase): ViewModel() {
    private val _validateLogin = MutableLiveData<UserEntity?>()
    val validateLogin: LiveData<UserEntity?> = _validateLogin

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun userLogin(email: String, password: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            loginUserUseCase.getUserByEmailAndPassword(email, password).collect { user ->
                if (user != null) {
                    _uiState.value = UiState.Success
                    _validateLogin.value = user
                    loginUserUseCase.saveUserSession(user)
                } else {
                    _uiState.value = UiState.Error("User not found!")
                }
            }
        }
    }
}