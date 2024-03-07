package com.girendi.tobamembers.core.domain.usecase

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.repository.UserRepository

class RegisterUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun registerUser(userEntity: User): Result<Boolean> =
        userRepository.insertUser(userEntity)

    suspend fun countUsersWithUsername(username: String): Result<Boolean> =
        userRepository.countUsersWithUsername(username)

    suspend fun countUsersWithEmail(email: String): Result<Boolean> =
        userRepository.countUsersWithEmail(email)
}