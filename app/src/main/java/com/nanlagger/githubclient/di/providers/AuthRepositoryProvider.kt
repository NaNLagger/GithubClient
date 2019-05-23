package com.nanlagger.githubclient.di.providers

import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.di.IoScheduler
import com.nanlagger.githubclient.di.UiScheduler
import com.nanlagger.githubclient.domain.repository.AuthRepository
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Provider

class AuthRepositoryProvider @Inject constructor(
        private val repositoryDao: RepositoryDao,
        @UiScheduler private val uiScheduler: Scheduler,
        @IoScheduler private val ioScheduler: Scheduler
) : Provider<AuthRepository> {
    override fun get(): AuthRepository {
        return AuthRepository(repositoryDao, uiScheduler, ioScheduler)
    }
}