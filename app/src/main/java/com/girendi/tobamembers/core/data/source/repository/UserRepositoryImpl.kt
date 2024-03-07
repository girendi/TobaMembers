package com.girendi.tobamembers.core.data.source.repository

import com.girendi.tobamembers.core.data.Result
import com.girendi.tobamembers.core.data.source.local.LocalDataSource
import com.girendi.tobamembers.core.domain.model.User
import com.girendi.tobamembers.core.domain.repository.UserRepository
import com.girendi.tobamembers.core.utils.DataMapperUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val localDataSource: LocalDataSource) : UserRepository {

    override fun getAllUser(): Flow<List<User>> =
        localDataSource.getAllUser()
            .map { entities -> entities.map { DataMapperUser.toDomainModel(it) } }

    override fun getUserByEmailAndPassword(email: String, password: String): Flow<User?> =
        localDataSource.getUserByEmailAndPassword(email, password)
            .map { DataMapperUser.toDomainModel(it!!) }

    override suspend fun deleteUser(userEntity: User) =
        localDataSource.deleteUser(DataMapperUser.toEntityModel(userEntity))

    override suspend fun updateUser(userEntity: User): Result<Boolean> {
        val isUsernameInvalid = localDataSource.countUsersWithUsername(userEntity.username, userEntity.id) > 0
        val isEmailInvalid = localDataSource.countUsersWithEmail(userEntity.email, userEntity.id) > 0

        return when {
            isUsernameInvalid || isEmailInvalid -> Result.Success(false)
            else -> {
                localDataSource.updateUser(DataMapperUser.toEntityModel(userEntity))
                Result.Success(true)
            }
        }
    }

    override suspend fun insertUser(userEntity: User): Result<Boolean> =
        localDataSource.insertUser(DataMapperUser.toEntityModel(userEntity)).toResult()

    override suspend fun countUsersWithUsername(username: String): Result<Boolean> =
        localDataSource.countUsersWithUsername(username).toResult()

    override suspend fun countUsersWithUsername(username: String, userId: Int): Result<Boolean> =
        localDataSource.countUsersWithUsername(username, userId).toResult()

    override suspend fun countUsersWithEmail(email: String): Result<Boolean> =
        localDataSource.countUsersWithEmail(email).toResult()

    override suspend fun countUsersWithEmail(email: String, userId: Int): Result<Boolean> =
        localDataSource.countUsersWithEmail(email, userId).toResult()

    override suspend fun validateUserByPassword(userId: Int, password: String): Result<Boolean> =
        localDataSource.validateUserByPassword(userId, password).toResult()

    private fun Long.toResult(): Result<Boolean> = try {
        Result.Success(this > 0)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private fun Int.toResult(): Result<Boolean> = try {
        Result.Success(this > 0)
    } catch (e: Exception) {
        Result.Error(e)
    }
}