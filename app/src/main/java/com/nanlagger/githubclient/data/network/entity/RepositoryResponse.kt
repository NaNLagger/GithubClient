package com.nanlagger.githubclient.data.network.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class RepositoryResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("description") val description: String?,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("stargazers_count") val starsCount: Int,
        @SerializedName("forks_count") val forksCount: Int,
        @SerializedName("owner") val user: UserResponse
)