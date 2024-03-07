package com.girendi.tobamembers.core.domain.usecase

import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import com.girendi.tobamembers.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow


class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val sessionManagerRepository: SessionManagerRepository
) {
    fun getUserByEmailAndPassword(email: String, password: String) : Flow<User?> =
        userRepository.getUserByEmailAndPassword(email, password)

    fun saveUserSession(userEntity: User) =
        sessionManagerRepository.saveUserSession(userEntity)
}