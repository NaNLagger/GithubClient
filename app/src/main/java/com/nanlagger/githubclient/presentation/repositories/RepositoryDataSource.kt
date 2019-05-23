package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.PageKeyedDataSource
import com.nanlagger.githubclient.tools.addTo
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.repository.GithubRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class RepositoryDataSource(
    private val githubRepository: GithubRepository
) : PageKeyedDataSource<Int, Repository>(), DataSourceWithState {

    override var query: String = ""
    override var loadInitialListener: (Boolean) -> Unit = {}
    override var errorHandler: (Throwable) -> Unit = {}

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repository>) {
        getRepositories(0, params.requestedLoadSize)
            .doOnSubscribe { loadInitialListener(true) }
            .subscribe({
                callback.onResult(it, null, 1)
                loadInitialListener(false)
            }, {
                Timber.e(it)
                loadInitialListener(false)
                errorHandler(it)
            })
            .addTo(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        getRepositories(params.key, params.requestedLoadSize)
            .subscribe({
                val nextKey = if (it.isEmpty()) null else params.key + 1
                callback.onResult(it, nextKey)
            }, {
                Timber.e(it)
                errorHandler(it)
            })
            .addTo(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        //ignore
    }

    private fun getRepositories(page: Int, perPage: Int): Single<List<Repository>> {
        return githubRepository.searchRepositories(query, page, perPage)
    }

    override fun clear() {
        compositeDisposable.dispose()
    }
}