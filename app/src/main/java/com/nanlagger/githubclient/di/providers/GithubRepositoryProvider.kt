package com.nanlagger.githubclient.di.providers

import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.data.network.GithubApi
import com.nanlagger.githubclient.di.IoScheduler
import com.nanlagger.githubclient.di.UiScheduler
import com.nanlagger.githubclient.domain.repository.GithubRepository
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Provider

class GithubRepositoryProvider @Inject constructor(
    private val githubApi: GithubApi,
    private val repositoryDao: RepositoryDao,
    @UiScheduler private val uiScheduler: Scheduler,
    @IoScheduler private val ioScheduler: Scheduler
) : Provider<GithubRepository> {
    override fun get(): GithubRepository {
        return GithubRepository(githubApi, repositoryDao, uiScheduler, ioScheduler)
    }
}