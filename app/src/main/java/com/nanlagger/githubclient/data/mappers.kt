package com.nanlagger.githubclient.data

import com.nanlagger.githubclient.data.database.entity.RepositoryEntity
import com.nanlagger.githubclient.data.database.entity.UserEntity
import com.nanlagger.githubclient.data.network.entity.RepositoryResponse
import com.nanlagger.githubclient.data.network.entity.UserResponse
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.entity.User
import java.util.*

fun UserResponse.toUser(): User {
    return User(this.id, this.login, this.avatarUrl)
}

fun UserEntity.toUser(): User {
    return User(this.id, this.login, this.avatar)
}

fun RepositoryResponse.toRepository(): Repository {
    return Repository(this.id, this.fullName, this.name, this.description ?: "", this.createdAt, this.starsCount, this.forksCount, this.user.toUser(), false)
}

fun RepositoryEntity.toRepository(): Repository {
    return Repository(this.id, this.fullName, this.name, this.description ?: "", Date(this.createdAt), this.starsCount, this.forksCount, this.owner!!.toUser(), true)
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(this.id, this.login, this.avatarUrl)
}

fun Repository.toRepositoryEntity(): RepositoryEntity {
    return RepositoryEntity(this.id, this.name, this.fullName, this.description, this.createdAt.time, this.starsCount, this.forksCount, this.user.toUserEntity())
}