package com.nanlagger.githubclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.data.database.entity.RepositoryEntity
import com.nanlagger.githubclient.data.database.entity.UserEntity

@Database(entities = [RepositoryEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRepositoryDao(): RepositoryDao
}