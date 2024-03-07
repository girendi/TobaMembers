package com.girendi.tobamembers.persentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.UiState
import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse
import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.usecase.MainUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val mainUseCase: MainUseCase) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _listPhoto = MutableLiveData<List<PhotoResponse>>()
    val listPhoto: LiveData<List<PhotoResponse>> = _listPhoto

    val userSession: LiveData<User?> = mainUseCase.getUserSession().asLiveData()

    private var currentPage = 1
    private var isLastPage = false

    init {
        fetchPhotos()
    }

    fun fetchPhotos() {
        if (isLastPage) {
            _uiState.postValue(UiState.Success)
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = mainUseCase.fetchPhoto(currentPage, 10)) {
                is Result.Success -> {
                    val photos =_listPhoto.value.orEmpty() + result.data
                    _listPhoto.value = photos
                    _uiState.postValue(UiState.Success)
                    isLastPage = result.data.isEmpty()
                    currentPage++
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "An unknown error occurred")
                }
                is Result.Loading -> {
                    _uiState.postValue(UiState.Success)
                }
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            mainUseCase.logoutUser()
        }
    }
}