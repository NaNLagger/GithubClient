package com.nanlagger.githubclient.domain.repository

import androidx.paging.PageKeyedDataSource
import com.nanlagger.githubclient.data.network.GithubApi
import com.nanlagger.githubclient.data.toRepository
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.tools.addTo
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class RepositoryDataSource(
    private val githubApi: GithubApi,
    private val query: String,
    private val uiScheduler: Scheduler,
    private val ioScheduler: Scheduler,
    override val callbacks: DataSourceWithState.Callbacks
) : PageKeyedDataSource<Int, Repository>(), DataSourceWithState {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Repository>) {
        load(0, params.requestedLoadSize, callbacks::loadInitial) {
            callback.onResult(it, null, 1)
            callbacks.loadZeroItem(it.isEmpty())
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        load(0, params.requestedLoadSize, callbacks::loadRange) {
            val nextKey = if (it.isEmpty()) null else params.key + 1
            callback.onResult(it, nextKey)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Repository>) {
        //ignore
    }

    override fun clear() {
        compositeDisposable.dispose()
    }

    private fun load(page: Int, perPage: Int, loadCallback: (Boolean) -> Unit, resultCallback: (List<Repository>) -> Unit) {
        if (query.isBlank()) {
            resultCallback(emptyList())
            loadCallback(false)
            return
        }
        githubApi.searchRepository(query, "", page = page, perPage = perPage)
            .map { response -> response.items.map { it.toRepository() } }
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