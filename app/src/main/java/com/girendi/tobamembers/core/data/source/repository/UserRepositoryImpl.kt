package com.girendi.tobamembers.core.data.source.repository

import android.database.sqlite.SQLiteConstraintException
import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.local.LocalDataSource
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val localDataSource: LocalDataSource) : UserRepository {

    override fun getAllUser(): Flow<List<UserEntity>> =
        localDataSource.getAllUser()

    override fun getUserByEmailAndPassword(email: String, password: String): Flow<UserEntity?> =
        localDataSource.getUserByEmailAndPassword(email, password)

    override suspend fun deleteUser(userEntity: UserEntity) =
        localDataSource.deleteUser(userEntity)

    override suspend fun updateUser(userEntity: UserEntity): Result<Boolean> =
        try {
            val invalidUsername = localDataSource.countUsersWithUsername(userEntity.username, userEntity.id) > 0
            val invalidEmail = localDataSource.countUsersWithEmail(userEntity.email, userEntity.id) > 0

            if(invalidUsername || invalidEmail) {
                Result.Success(false)
            } else {
                localDataSource.updateUser(userEntity)
                Result.Success(true)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun insertUser(userEntity: UserEntity): Result<Boolean> =
        try {
            val status = localDataSource.insertUser(userEntity) > 0
            Result.Success(status)
        } catch (e: SQLiteConstraintException) {
            Result.Error(e)
        }

    override suspend fun countUsersWithUsername(username: String): Result<Boolean> =
        try {
            val status = localDataSource.countUsersWithUsername(username) > 0
            Result.Success(status)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun countUsersWithUsername(username: String, userId: Int): Result<Boolean> =
        try {
            val status = localDataSource.countUsersWithUsername(username, userId) > 0
            Result.Success(status)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun countUsersWithEmail(email: String): Result<Boolean> =
        try {
            val status = localDataSource.countUsersWithEmail(email) > 0
            Result.Success(status)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun countUsersWithEmail(email: String, userId: Int): Result<Boolean> =
        try {
            val status = localDataSource.countUsersWithEmail(email, userId) > 0
            Result.Success(status)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun validateUserByPassword(userId: Int, password: String): Result<Boolean> =
        try {
            val status = localDataSource.validateUserByPassword(userId, password) > 0
            Result.Success(status)
        } catch (e: Exception) {
            Result.Error(e)
        }
}