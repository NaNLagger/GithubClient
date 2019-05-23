package com.nanlagger.githubclient.data.network

import com.nanlagger.githubclient.data.network.entity.RepositoryResponse
import com.nanlagger.githubclient.data.network.entity.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    fun searchRepository(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("order") order: String = "desc",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100): Single<SearchResponse>

    @GET("repos/{owner}/{name}")
    fun getRepository(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): Single<RepositoryResponse>
}