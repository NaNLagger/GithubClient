package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.nanlagger.githubclient.Screens
import com.nanlagger.githubclient.domain.entity.Repository
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class RepositoriesListPresenter @Inject constructor(
    private val repositoryDataSourceFactory: RepositoryDataSourceFactory,
    private val router: Router
) : MvpPresenter<RepositoriesListView>() {

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .build()
    private lateinit var currentDataSource: DataSource<Int, Repository>
    private var currentQuery: String = ""

    override fun onFirstViewAttach() {
        createDataSource("")
        viewState.setItems(createPagedList(currentDataSource))
    }

    fun refresh() {
        currentDataSource.invalidate()
        createDataSource(currentQuery)
        viewState.setItems(createPagedList(currentDataSource))
    }

    fun submitQuery(query: String?) {
        currentQuery = query ?: ""
        currentDataSource.invalidate()
        createDataSource(currentQuery)
        viewState.setItems(createPagedList(currentDataSource))
    }

    fun openRepository(repository: Repository) {
        router.navigateTo(Screens.RepositoryInfoScreen(repository.fullName))
    }

    private fun createPagedList(dataSource: DataSource<Int, Repository>) =
        PagedList.Builder(dataSource, config)
            .setFetchExecutor { it.run() }
            .setNotifyExecutor { it.run() }
            .build()

    private fun createDataSource(query: String) {
        currentDataSource = repositoryDataSourceFactory.create()
            .apply {
                if (this is DataSourceWithState) {
                    this.query = query
                    loadInitialListener = { loading -> viewState.setLoading(loading) }
                    errorHandler = { error -> viewState.showError(error.localizedMessage) }
                }
            }
    }
}