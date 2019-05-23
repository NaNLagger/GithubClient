package com.nanlagger.githubclient.di.module

import com.nanlagger.githubclient.data.database.AppDatabase
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.di.providers.AppDatabaseProvider
import com.nanlagger.githubclient.di.providers.RepositoryDaoProvider
import toothpick.config.Module

class DatabaseModule : Module() {
    init {
        bind(AppDatabase::class.java).toProvider(AppDatabaseProvider::class.java).providesSingletonInScope()
        bind(RepositoryDao::class.java).toProvider(RepositoryDaoProvider::class.java).providesSingletonInScope()
    }
}