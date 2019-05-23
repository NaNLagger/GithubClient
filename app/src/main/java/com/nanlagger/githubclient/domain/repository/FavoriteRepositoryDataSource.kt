package com.nanlagger.githubclient.domain.repository

import androidx.paging.PositionalDataSource
import com.nanlagger.githubclient.data.database.dao.RepositoryDao
import com.nanlagger.githubclient.data.toRepository
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.tools.addTo
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class FavoriteRepositoryDataSource(
        private val repositoryDao: RepositoryDao,
        private val uiScheduler: Scheduler,
        private val ioScheduler: Scheduler,
        override val callbacks: DataSourceWithState.Callbacks
) : PositionalDataSource<Repository>(), DataSourceWithState {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Repository>) {
        load(params.startPosition, params.loadSize, callbacks::loadRange) {
            callback.onResult(it)
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Repository>) {
        load(params.requestedStartPosition, params.requestedLoadSize, callbacks::loadInitial) {
            callback.onResult(it, params.requestedStartPosition)
            callbacks.loadZeroItem(it.isEmpty())
        }
    }

    override fun clear() {
        compositeDisposable.clear()
    }

    private fun load(offset: Int, limit: Int, loadCallback: (Boolean) -> Unit, resultCallback: (List<Repository>) -> Unit) {
        repositoryDao.getRepositories(offset, limit)
                .map { entities -> entities.map { it.toRepository() } }
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnSubscribe { loadCallback(true) }
                .subscribe({
                    resultCallback(it)
                    loadCallback(false)
                }, {
                    Timber.e(it)
                    loadCallback(false)
                    callbacks.error(it)
                })
                .addTo(compositeDisposable)
    }
}