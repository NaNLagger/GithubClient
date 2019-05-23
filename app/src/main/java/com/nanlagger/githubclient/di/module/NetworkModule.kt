package com.nanlagger.githubclient.di.module

import com.nanlagger.githubclient.data.network.CurlLoggingInterceptor
import com.nanlagger.githubclient.data.network.GithubApi
import com.nanlagger.githubclient.di.providers.GithubApiProvider
import com.nanlagger.githubclient.di.providers.OkHttpClientProvider
import com.nanlagger.githubclient.di.providers.RetrofitProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import toothpick.config.Module

class NetworkModule : Module() {
    init {
        bind(HttpLoggingInterceptor::class.java).toInstance(HttpLoggingInterceptor())
        bind(CurlLoggingInterceptor::class.java).toInstance(CurlLoggingInterceptor())
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).providesSingletonInScope()
        bind(GithubApi::class.java).toProvider(GithubApiProvider::class.java).providesSingletonInScope()
    }
}