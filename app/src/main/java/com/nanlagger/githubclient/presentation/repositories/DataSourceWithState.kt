package com.nanlagger.githubclient.presentation.repositories

interface DataSourceWithState {

    var query: String
    var loadInitialListener: (Boolean) -> Unit
    var errorHandler: (Throwable) -> Unit

    fun clear()
}