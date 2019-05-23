package com.nanlagger.githubclient.data.database.entity

import androidx.room.ColumnInfo

class UserEntity(
    @ColumnInfo(name = ID) var id: Long = 0L,
    @ColumnInfo(name = LOGIN) var login: String = "",
    @ColumnInfo(name = AVATAR) var avatar: String = ""
) {

    companion object {
        const val ID = "id"
        const val LOGIN = "login"
        const val AVATAR = "avatar"
    }
}