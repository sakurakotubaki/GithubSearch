package com.junichi.githubsearch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<GitHubRepo>
)

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "owner") val owner: Owner,
    @Json(name = "description") val description: String?,
    @Json(name = "language") val language: String?,
    @Json(name = "stargazers_count") val stargazersCount: Int,
    @Json(name = "watchers_count") val watchersCount: Int,
    @Json(name = "forks_count") val forksCount: Int,
    @Json(name = "open_issues_count") val openIssuesCount: Int
)

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String
)
