package com.nanlagger.githubclient.data.database.entity

import androidx.room.*
import com.nanlagger.githubclient.data.database.entity.RepositoryEntity.Companion.FULL_NAME
import com.nanlagger.githubclient.data.database.entity.RepositoryEntity.Companion.TABLE_NAME


@Entity(
    tableName = TABLE_NAME,
    indices = [Index(FULL_NAME, unique = true)]
)
class RepositoryEntity(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long = 0L,
    @ColumnInfo(name = NAME) var name: String = "",
    @ColumnInfo(name = FULL_NAME) var fullName: String = "",
    @ColumnInfo(name = DESCRIPTION) var description: String? = null,
    @ColumnInfo(name = CREATED_AT) var createdAt: Long = 0L,
    @ColumnInfo(name = STARS_COUNT) var starsCount: Int = 0,
    @ColumnInfo(name = FORKS_COUNT) var forksCount: Int = 0,
    @Embedded(prefix = OWNER) var owner: UserEntity? = null
) {

    companion object {
        const val TABLE_NAME = "repository"

        const val ID = "id"
        const val NAME = "name"
        const val FULL_NAME = "full_name"
        const val DESCRIPTION = "description"
        const val CREATED_AT = "created_at"
        const val STARS_COUNT = "stars_count"
        const val FORKS_COUNT = "forks_count"
        const val OWNER = "owner"
    }
}