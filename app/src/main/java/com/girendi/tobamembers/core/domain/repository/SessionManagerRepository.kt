package com.girendi.tobamembers.core.domain.repository

import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface SessionManagerRepository {
    fun saveUserSession(userSession: UserEntity)
    fun getUserSession(): Flow<UserEntity?>
    fun clearSession()
}