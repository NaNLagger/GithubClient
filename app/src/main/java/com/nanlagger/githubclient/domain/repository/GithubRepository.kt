package com.nanlagger.githubclient.domain.repository

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.data.network.GithubApi
import com.nanlagger.githubclient.data.toRepository
import com.nanlagger.githubclient.data.toRepositoryEntity
import com.nanlagger.githubclient.domain.entity.Repository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class GithubRepository(
    private val githubApi: GithubApi,
    private val repositoryDao: RepositoryDao,
    private val uiScheduler: Scheduler,
    private val ioScheduler: Scheduler
) {

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .build()

    private val updateRelay: PublishSubject<Boolean> = PublishSubject.create()

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

    fun searchRepositories(query: String, callbacks: DataSourceWithState.Callbacks): Observable<PagedList<Repository>> {
        return Observable.fromCallable {
            RepositoryDataSource(githubApi, query, uiScheduler, ioScheduler, callbacks)
        }
            .map { createPagedList(it) }
    }

    fun getFavoriteRepositories(callbacks: DataSourceWithState.Callbacks): Observable<PagedList<Repository>> {
        val firstObservable = Observable.fromCallable {
            FavoriteRepositoryDataSource(repositoryDao, uiScheduler, ioScheduler, callbacks)
        }
        val updateObservable = updateRelay
            .map { FavoriteRepositoryDataSource(repositoryDao, uiScheduler, ioScheduler, callbacks) }
        return Observable.merge(firstObservable, updateObservable)
            .map { createPagedList(it) }
    }

    fun addToFavorite(repository: Repository): Completable {
        return Completable.fromAction { repositoryDao.insert(repository.toRepositoryEntity()) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnComplete { updateRelay.onNext(true) }
    }

    fun deleteFromFavorite(repository: Repository): Completable {
        return Completable.fromAction { repositoryDao.delete(repository.toRepositoryEntity()) }
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnComplete { updateRelay.onNext(true) }
    }

    private fun createPagedList(dataSource: DataSource<Int, Repository>) =
        PagedList.Builder(dataSource, config)
            .setFetchExecutor { it.run() }
            .setNotifyExecutor { it.run() }
            .build()
}