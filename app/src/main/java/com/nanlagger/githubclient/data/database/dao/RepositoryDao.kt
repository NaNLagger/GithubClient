package com.nanlagger.githubclient.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.nanlagger.githubclient.data.database.entity.RepositoryEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RepositoryDao : BaseDao<RepositoryEntity> {

    @Query("SELECT * FROM ${RepositoryEntity.TABLE_NAME} WHERE ${RepositoryEntity.FULL_NAME} = :fullName LIMIT 1")
    fun getRepositoryByFullName(fullName: String) : Single<RepositoryEntity>

    @Query("SELECT * FROM ${RepositoryEntity.TABLE_NAME} WHERE ${RepositoryEntity.FULL_NAME} LIKE '%' || :search  || '%' or ${RepositoryEntity.DESCRIPTION} LIKE '%' || :search  || '%'")
    fun getRepositories(search: String): Single<List<RepositoryEntity>>

    @Query("SELECT * FROM ${RepositoryEntity.TABLE_NAME} LIMIT :offset, :limit")
    fun getRepositories(offset: Int, limit: Int): Single<List<RepositoryEntity>>

    @Query("DELETE FROM ${RepositoryEntity.TABLE_NAME}")
    fun clearRepositories()
}