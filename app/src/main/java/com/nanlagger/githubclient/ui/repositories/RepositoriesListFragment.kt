package com.nanlagger.githubclient.ui.repositories

import android.os.Bundle
import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.nanlagger.githubclient.R
import com.nanlagger.githubclient.di.FavoriteFlag
import com.nanlagger.githubclient.di.PrimitiveWrapper
import com.nanlagger.githubclient.di.Scopes
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.presentation.repositories.RepositoriesListPresenter
import com.nanlagger.githubclient.presentation.repositories.RepositoriesListView
import com.nanlagger.githubclient.presentation.repositories.RepositoryDataSourceFactory
import com.nanlagger.githubclient.tools.argument
import com.nanlagger.githubclient.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_repositories.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module

class RepositoriesListFragment : BaseFragment(), RepositoriesListView {

    override val layoutId: Int = R.layout.fragment_repositories

    private val isFavorite by argument(KEY_IS_FAVORITE, false)

    @InjectPresenter
    lateinit var presenter: RepositoriesListPresenter

    @ProvidePresenter
    fun providePresenter(): RepositoriesListPresenter =
        scope.getInstance(RepositoriesListPresenter::class.java)

    private lateinit var scope: Scope
    private val adapter: RepositoryAdapter by lazy {
        RepositoryAdapter(
            presenter::openRepository,
            RepostoriesDiffCallback()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefresh.setOnRefreshListener { presenter.refresh() }
        recyclerRepositories.layoutManager = LinearLayoutManager(context)
        recyclerRepositories.adapter = adapter
    }

    override fun onOpenScope() {
        scope = Toothpick.openScopes(Scopes.APP_SCOPE, Scopes.REPOSITORIES_LIST_SCOPE + fragmentId)
            .apply {
                installModules(object : Module() {
                    init {
                        bind(RepositoryDataSourceFactory::class.java)
                        bind(PrimitiveWrapper::class.java)
                            .withName(FavoriteFlag::class.java)
                            .toInstance(PrimitiveWrapper(isFavorite))
                    }
                })
            }
    }

    override fun onCloseScope() {
        Toothpick.closeScope(Scopes.REPOSITORIES_LIST_SCOPE + fragmentId)
    }

    override fun setLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun setItems(items: PagedList<Repository>) {
        adapter.submitList(items)
    }

    override fun showError(message: String) {
        showSnackMessage(message)
    }

    fun submitQuery(query: String?) {
        presenter.submitQuery(query)
    }

    companion object {
        const val KEY_IS_FAVORITE = "key.is.favorite"
        fun newInstance(isFavorite: Boolean): RepositoriesListFragment {
            return RepositoriesListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_IS_FAVORITE, isFavorite)
                }
            }
        }
    }
}