package com.girendi.tobamembers.core.domain.repository

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUser(): Flow<List<User>>

    fun getUserByEmailAndPassword(email: String, password: String): Flow<User?>

    suspend fun deleteUser(userEntity: User)

    suspend fun updateUser(userEntity: User): Result<Boolean>

    suspend fun insertUser(userEntity: User): Result<Boolean>

    suspend fun countUsersWithUsername(username: String): Result<Boolean>

    suspend fun countUsersWithUsername(username: String, userId: Int): Result<Boolean>

    suspend fun countUsersWithEmail(email: String): Result<Boolean>

    suspend fun countUsersWithEmail(email: String, userId: Int): Result<Boolean>

    suspend fun validateUserByPassword(userId: Int, password: String): Result<Boolean>
}