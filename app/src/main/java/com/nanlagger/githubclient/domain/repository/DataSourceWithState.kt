package com.nanlagger.githubclient.domain.repository

interface DataSourceWithState {

    val callbacks: Callbacks

    fun clear()

    abstract class Callbacks {
        open fun loadInitial(loading: Boolean) {}
        open fun loadRange(loading: Boolean) {}
        open fun error(throwable: Throwable) {}
        open fun loadZeroItem(isEmpty: Boolean) {}
    }
}