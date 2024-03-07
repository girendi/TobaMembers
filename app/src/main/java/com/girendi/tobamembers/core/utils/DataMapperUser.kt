package com.girendi.tobamembers.core.utils

import com.girendi.tobamembers.core.data.source.local.entity.UserEntity
import com.girendi.tobamembers.core.domain.model.User

object DataMapperUser {
    fun toDomainModel(userEntity: UserEntity): User =
        User(
            id = userEntity.id,
            username = userEntity.username,
            email = userEntity.email,
            password = userEntity.password,
            role = userEntity.role
        )

    fun toEntityModel(user: User): UserEntity =
        UserEntity(
            id = user.id,
            username = user.username,
            email = user.email,
            password = user.password,
            role = user.role
        )
}