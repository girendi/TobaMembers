package com.girendi.tobamembers.core.domain.repository

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    fun getAllUser(): Flow<List<UserEntity>>

    fun getUserByEmailAndPassword(email: String, password: String): Flow<UserEntity?>

    suspend fun deleteUser(userEntity: UserEntity)

    suspend fun updateUser(userEntity: UserEntity): Result<Boolean>

    suspend fun insertUser(userEntity: UserEntity): Result<Boolean>

    suspend fun countUsersWithUsername(username: String): Result<Boolean>

    suspend fun countUsersWithUsername(username: String, userId: Int): Result<Boolean>

    suspend fun countUsersWithEmail(email: String): Result<Boolean>

    suspend fun countUsersWithEmail(email: String, userId: Int): Result<Boolean>

    suspend fun validateUserByPassword(userId: Int, password: String): Result<Boolean>
}