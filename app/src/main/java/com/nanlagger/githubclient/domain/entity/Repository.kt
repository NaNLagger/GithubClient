package com.nanlagger.githubclient.domain.entity

import java.util.*

data class Repository(
    val id: Long,
    val fullName: String,
    val name: String,
    val description: String,
    val createdAt: Date,
    val starsCount: Int,
    val forksCount: Int,
    val user: User,
    val isFavorite: Boolean
)