package com.girendi.tobamembers.core.data.source.local

import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.data.source.local.room.UserEntityDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val userEntityDao: UserEntityDao) {
    fun getAllUser(): Flow<List<UserEntity>> =
        userEntityDao.getAllUser()

    fun getUserByEmailAndPassword(email: String, password: String): Flow<UserEntity?> =
        userEntityDao.getUserByEmailAndPassword(email, password)

    suspend fun deleteUser(userEntity: UserEntity) =
        userEntityDao.deleteUser(userEntity)

    suspend fun updateUser(userEntity: UserEntity) =
        userEntityDao.updateUser(userEntity)

    suspend fun insertUser(userEntity: UserEntity): Long =
        userEntityDao.insertUser(userEntity)

    suspend fun countUsersWithUsername(username: String): Int =
        userEntityDao.countUsersWithUsername(username)

    suspend fun countUsersWithUsername(username: String, userId: Int): Int =
        userEntityDao.countUsersWithUsername(username, userId)

    suspend fun countUsersWithEmail(email: String): Int =
        userEntityDao.countUsersWithEmail(email)

    suspend fun countUsersWithEmail(email: String, userId: Int): Int =
        userEntityDao.countUsersWithEmail(email, userId)

    suspend fun validateUserByPassword(userId: Int, password: String): Int =
        userEntityDao.validateUserByPassword(userId, password)
}