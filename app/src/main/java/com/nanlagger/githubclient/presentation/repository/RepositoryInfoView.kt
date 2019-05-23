package com.nanlagger.githubclient.presentation.repository

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.nanlagger.githubclient.domain.entity.Repository

interface RepositoryInfoView : MvpView {

    @StateStrategyType(SingleStateStrategy::class)
    fun setLoading(loading:Boolean)

    @StateStrategyType(SingleStateStrategy::class)
    fun showRepository(repository: Repository)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(error: String)
}