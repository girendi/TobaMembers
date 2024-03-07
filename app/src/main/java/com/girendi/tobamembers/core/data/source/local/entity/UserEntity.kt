package com.girendi.tobamembers.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user", indices = [
    Index(value = ["username"], unique = true),
    Index(value = ["email"], unique = true),
])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "email")
    var email: String,
    @ColumnInfo(name = "password")
    var password: String,
    @ColumnInfo(name = "role")
    var role: String,
)
