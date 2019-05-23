package com.nanlagger.githubclient.data.network.entity

import com.google.gson.annotations.SerializedName

data class SearchResponse(
        @SerializedName("total_count") val totalCount: Int,
        @SerializedName("incomplete_results") val incompleteResult: Boolean,
        @SerializedName("items") val items: List<RepositoryResponse>
)