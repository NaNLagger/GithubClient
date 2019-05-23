package com.nanlagger.githubclient.di.providers

import com.nanlagger.githubclient.data.network.CurlLoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
    private val loggingInterceptor: HttpLoggingInterceptor,
    private val curlLoggingInterceptor: CurlLoggingInterceptor
) : Provider<OkHttpClient> {

    override fun get() = with(OkHttpClient.Builder()) {
        addNetworkInterceptor(loggingInterceptor)
        addNetworkInterceptor(curlLoggingInterceptor)
        build()
    }
}