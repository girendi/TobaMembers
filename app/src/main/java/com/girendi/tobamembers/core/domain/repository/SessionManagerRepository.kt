package com.girendi.tobamembers.core.domain.repository

import com.girendi.tobamembers.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SessionManagerRepository {
    fun saveUserSession(userSession: User)
    fun getUserSession(): Flow<User?>
    fun clearSession()
}