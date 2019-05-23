package com.nanlagger.githubclient.di.module

import com.nanlagger.githubclient.di.providers.GithubRepositoryProvider
import com.nanlagger.githubclient.domain.repository.AuthRepository
import com.nanlagger.githubclient.domain.repository.GithubRepository
import toothpick.config.Module

class RepositoryModule : Module() {
    init {
        bind(AuthRepository::class.java).toInstance(AuthRepository())
        bind(GithubRepository::class.java).toProvider(GithubRepositoryProvider::class.java).singletonInScope()
    }
}