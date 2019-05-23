package com.nanlagger.githubclient.di.providers

import com.nanlagger.githubclient.data.database.AppDatabase
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import javax.inject.Inject
import javax.inject.Provider

class RepositoryDaoProvider @Inject constructor(
    private val appDatabase: AppDatabase
) : Provider<RepositoryDao> {
    override fun get(): RepositoryDao {
        return appDatabase.getRepositoryDao()
    }
}