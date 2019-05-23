package com.nanlagger.githubclient.di.providers

import com.nanlagger.githubclient.data.network.GithubApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class GithubApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<GithubApi> {
    override fun get(): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}