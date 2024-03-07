package com.girendi.tobamembers.core.domain.usecase

import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import kotlinx.coroutines.flow.Flow

class SplashUseCase(private val sessionManagerRepository: SessionManagerRepository) {
    fun getUserSession(): Flow<User?> =
        sessionManagerRepository.getUserSession()
}