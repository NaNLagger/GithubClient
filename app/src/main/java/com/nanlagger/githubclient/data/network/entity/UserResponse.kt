package com.nanlagger.githubclient.data.network.entity

import com.google.gson.annotations.SerializedName

data class UserResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String
)