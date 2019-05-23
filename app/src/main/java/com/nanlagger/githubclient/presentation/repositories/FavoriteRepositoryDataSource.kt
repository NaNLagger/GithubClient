package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.PositionalDataSource
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.repository.GithubRepository
import com.nanlagger.githubclient.tools.addTo
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class FavoriteRepositoryDataSource(
    private val githubRepository: GithubRepository
) : PositionalDataSource<Repository>(), DataSourceWithState {
    override var query: String = ""
    override var loadInitialListener: (Boolean) -> Unit = {}
    override var errorHandler: (Throwable) -> Unit ={}

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Repository>) {
        githubRepository.getFavoriteRepositories(params.startPosition, params.loadSize)
            .doOnSubscribe { loadInitialListener(true) }
            .subscribe({
                callback.onResult(it)
                loadInitialListener(false)
            }, {
                Timber.e(it)
                loadInitialListener(false)
                errorHandler(it)
            })
            .addTo(compositeDisposable)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Repository>) {
        githubRepository.getFavoriteRepositories(params.requestedStartPosition, params.requestedLoadSize)
            .doOnSubscribe { loadInitialListener(true) }
            .subscribe({
                callback.onResult(it, params.requestedStartPosition)
                loadInitialListener(false)
            }, {
                Timber.e(it)
                loadInitialListener(false)
                errorHandler(it)
            })
            .addTo(compositeDisposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}