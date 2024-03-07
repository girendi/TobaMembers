package com.girendi.tobamembers.core.domain.usecase

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.data.source.remote.response.PhotoResponse
import com.girendi.tobamembers.core.domain.repository.PhotoRepository
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import kotlinx.coroutines.flow.Flow

class MainUseCase(
    private val photoRepository: PhotoRepository,
    private val sessionManagerRepository: SessionManagerRepository
) {
    suspend fun fetchPhoto(page: Int, limit: Int): Result<List<PhotoResponse>> =
        photoRepository.fetchListPhoto(page, limit)

    fun getUserSession(): Flow<UserEntity?> =
        sessionManagerRepository.getUserSession()

    fun logoutUser() =
        sessionManagerRepository.clearSession()
}