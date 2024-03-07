package com.girendi.tobamembers.persentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.usecase.SplashUseCase

class SplashViewModel(splashUseCase: SplashUseCase): ViewModel() {
    val userSession: LiveData<User?> = splashUseCase.getUserSession().asLiveData()
}