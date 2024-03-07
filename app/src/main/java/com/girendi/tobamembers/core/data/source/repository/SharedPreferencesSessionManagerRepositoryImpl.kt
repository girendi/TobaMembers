package com.girendi.tobamembers.core.data.source.repository

import android.content.Context
import android.content.SharedPreferences
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.repository.SessionManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPreferencesSessionManagerRepositoryImpl(private val context: Context): SessionManagerRepository {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("UserSessionPrefs", Context.MODE_PRIVATE)
    }

    companion object {
        const val USER_ID_KEY = "user_id"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
        const val ROLE_KEY = "role"
    }

    override fun saveUserSession(userSession: UserEntity) {
        with(prefs.edit()) {
            putInt(USER_ID_KEY, userSession.id)
            putString(USERNAME_KEY, userSession.username)
            putString(EMAIL_KEY, userSession.email)
            putString(ROLE_KEY, userSession.role)
            apply()
        }
    }

    override fun getUserSession(): Flow<UserEntity?> = flow {
        val id = prefs.getInt(USER_ID_KEY, -1)
        if (id == -1) {
            emit(null)
        } else {
            val username = prefs.getString(USERNAME_KEY, null)
            val email = prefs.getString(EMAIL_KEY, null)
            val role = prefs.getString(ROLE_KEY, null) ?: ""
            if (username != null && email != null) {
                emit(
                    UserEntity(
                        id = id,
                        username = username,
                        email = email,
                        password = "",
                        role = role
                        )
                )
            } else {
                emit(null)
            }
        }
    }

    override fun clearSession() {
        prefs.edit().clear().apply()
    }
}