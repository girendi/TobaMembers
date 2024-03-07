package com.girendi.tobamembers.persentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.usecase.AdminUseCase
import kotlinx.coroutines.launch

class AdminViewModel(private val adminUseCase: AdminUseCase): ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    val users: LiveData<List<UserEntity>> = adminUseCase.getAllUser().asLiveData()

    val userSession: LiveData<UserEntity?> = adminUseCase.getUserSession().asLiveData()

    fun logoutUser() {
        viewModelScope.launch {
            adminUseCase.logoutUser()
        }
    }

    fun validateUserPassword(userId: Int, password: String, item: UserEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = adminUseCase.validateUserByPassword(userId, password)) {
                is Result.Success -> {
                    if (result.data){
                        deleteUser(item)
                        _uiState.value = UiState.Success
                    } else {
                        _uiState.value = UiState.Error("Your password is wrong!")
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

    private fun deleteUser(item: UserEntity) {
        viewModelScope.launch {
            adminUseCase.deleteUser(item)
        }
    }

    fun updateUser(item: UserEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = adminUseCase.updateUser(item)) {
                is Result.Success -> {
                    if (result.data) {
                        _uiState.value = UiState.Success
                    } else {
                        _uiState.value = UiState.Error("Username and email already exist!")
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
}