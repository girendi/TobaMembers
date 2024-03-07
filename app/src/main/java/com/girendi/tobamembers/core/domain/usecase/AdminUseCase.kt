package com.girendi.tobamembers.core.domain.usecase

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import com.girendi.tobamembers.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class AdminUseCase(
    private val userRepository: UserRepository,
    private val sessionManagerRepository: SessionManagerRepository
) {
    fun getAllUser(): Flow<List<UserEntity>> =
        userRepository.getAllUser()

    fun getUserSession(): Flow<UserEntity?> =
        sessionManagerRepository.getUserSession()

    fun logoutUser() =
        sessionManagerRepository.clearSession()

    suspend fun validateUserByPassword(userId: Int, password: String): Result<Boolean> =
        userRepository.validateUserByPassword(userId, password)

    suspend fun deleteUser(user: UserEntity) =
        userRepository.deleteUser(user)

    suspend fun updateUser(user: UserEntity): Result<Boolean> =
        userRepository.updateUser(user)
}