package com.nanlagger.githubclient.presentation.repositories

import androidx.paging.DataSource
import com.nanlagger.githubclient.di.FavoriteFlag
import com.nanlagger.githubclient.di.PrimitiveWrapper
import com.nanlagger.githubclient.domain.entity.Repository
import com.nanlagger.githubclient.domain.repository.GithubRepository
import javax.inject.Inject

class RepositoryDataSourceFactory @Inject constructor(
    private val githubRepository: GithubRepository,
    @FavoriteFlag private val isFavorite: PrimitiveWrapper<Boolean>
) : DataSource.Factory<Int, Repository>() {
    override fun create(): DataSource<Int, Repository> {
        return if (isFavorite.value)
            FavoriteRepositoryDataSource(githubRepository)
        else
            RepositoryDataSource(githubRepository)
    }
}