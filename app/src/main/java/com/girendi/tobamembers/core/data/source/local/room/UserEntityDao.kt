package com.girendi.tobamembers.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEntityDao {
    @Query("SELECT * FROM user ORDER BY id")
    fun getAllUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM User WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailAndPassword(email: String, password: String): Flow<UserEntity?>

    @Query("SELECT COUNT(id) FROM User WHERE username = :username AND id != :userId")
    suspend fun countUsersWithUsername(username: String, userId: Int): Int

    @Query("SELECT COUNT(id) FROM User WHERE username = :username")
    suspend fun countUsersWithUsername(username: String): Int

    @Query("SELECT COUNT(id) FROM User WHERE email = :email AND id != :userId")
    suspend fun countUsersWithEmail(email: String, userId: Int): Int

    @Query("SELECT COUNT(id) FROM User WHERE email = :email")
    suspend fun countUsersWithEmail(email: String): Int

    @Query("SELECT COUNT(id) FROM User WHERE id = :userId AND password = :password")
    suspend fun validateUserByPassword(userId: Int, password: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}