package com.nanlagger.githubclient.domain.repository

import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.data.network.GithubApi
import com.nanlagger.githubclient.data.toRepository
import com.nanlagger.githubclient.data.toRepositoryEntity
import com.nanlagger.githubclient.domain.entity.Repository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class GithubRepository(
    private val githubApi: GithubApi,
    private val repositoryDao: RepositoryDao,
    private val uiScheduler: Scheduler,
    private val ioScheduler: Scheduler
) {

    fun getRepository(fullName: String): Single<Repository> {
        return repositoryDao.getRepositoryByFullName(fullName)
            .map { it.toRepository() }
            .onErrorResumeNext {
                val (owner, name) = fullName.split('/')
                githubApi.getRepository(owner, name)
                    .map { it.toRepository() }
            }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
    }

    fun searchRepositories(query: String, page: Int, perPage: Int): Single<List<Repository>> {
        return if (query.isEmpty())
            Single.just(emptyList())
        else
            githubApi.searchRepository(query, "desc", page = page, perPage = perPage)
                .map { response -> response.items.map { it.toRepository() } }
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
    }

    fun getFavoriteRepositories(offset: Int, limit: Int): Single<List<Repository>> {
        return repositoryDao.getRepositories(offset, limit)
            .map { entities -> entities.map { it.toRepository() } }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
    }

    fun addToFavorite(repository: Repository): Completable {
        return Completable.fromAction { repositoryDao.insert(repository.toRepositoryEntity()) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
    }

    fun deleteFromFavorite(repository: Repository): Completable {
        return Completable.fromAction { repositoryDao.delete(repository.toRepositoryEntity()) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
    }
}