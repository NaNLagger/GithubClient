package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.PagedList
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.nanlagger.githubclient.domain.entity.Repository

interface RepositoriesListView: MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setLoading(loading: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setItems(items: PagedList<Repository>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(message: String)
}