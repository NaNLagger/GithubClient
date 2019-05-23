package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.nanlagger.githubclient.Screens
import com.nanlagger.githubclient.di.FavoriteFlag
import com.nanlagger.githubclient.di.PrimitiveWrapper
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.repository.DataSourceWithState
import com.nanlagger.githubclient.domain.repository.GithubRepository
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class RepositoriesListPresenter @Inject constructor(
    private val githubRepository: GithubRepository,
    @FavoriteFlag private val isFavorite: PrimitiveWrapper<Boolean>,
    private val router: Router
) : MvpPresenter<RepositoriesListView>() {

    private var currentDataSource: DataSource<*, Repository>? = null
    private var currentQuery: String = ""
    private var fetchDisposable: Disposable? = null
    private val callbacks = object : DataSourceWithState.Callbacks() {
        override fun loadInitial(loading: Boolean) {
            viewState.setLoading(loading)
        }

        override fun error(throwable: Throwable) {
            viewState.showError(throwable.localizedMessage)
        }

        override fun loadZeroItem(isEmpty: Boolean) {
            viewState.setEmpty(isEmpty)
        }
    }


    override fun onFirstViewAttach() {
        fetch()
    }

    fun refresh() {
        fetch()
    }

    fun submitQuery(query: String?) {
        currentQuery = query ?: ""
        fetch()
    }

    fun openRepository(repository: Repository) {
        router.navigateTo(Screens.RepositoryInfoScreen(repository.fullName))
    }

    private fun clearDataSource() {
        currentDataSource?.let {
            it.invalidate()
            if (it is DataSourceWithState) {
                it.clear()
            }
        }
    }

    private fun searchRepository(): Observable<PagedList<Repository>> {
        return if (isFavorite.value) {
            githubRepository.getFavoriteRepositories(callbacks)
        } else {
            githubRepository.searchRepositories(currentQuery, callbacks)
        }
    }

    private fun fetch() {
        fetchDisposable?.dispose()
        fetchDisposable = searchRepository()
            .subscribe({
                clearDataSource()
                currentDataSource = it.dataSource
                viewState.setItems(it)
            }, {
                Timber.e(it)
            }, {
                Timber.d("onComplete")
            })
    }

    override fun onDestroy() {
        clearDataSource()
        fetchDisposable?.dispose()
    }

}