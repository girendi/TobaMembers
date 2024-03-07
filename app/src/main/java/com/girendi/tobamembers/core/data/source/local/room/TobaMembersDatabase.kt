package com.girendi.tobamembers.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.girendi.tobamembers.core.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class TobaMembersDatabase : RoomDatabase() {
    abstract fun userEntityDao(): UserEntityDao
}