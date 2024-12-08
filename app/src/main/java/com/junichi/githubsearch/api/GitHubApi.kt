package com.junichi.githubsearch.api

import com.junichi.githubsearch.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

// GitHub API を呼び出すためのインターフェイス
interface GitHubApi {
    // GitHub API の検索エンドポイント
    @GET("search/repositories")
    suspend fun searchRepositories(
        // @Query はリクエストパラメータ
        // @Query("q") はリクエストパラメータ名
        @Query("q") query: String
    ): SearchResponse
}
