package com.junichi.githubsearch.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// このクラスは JSON を Kotlin オブジェクトに変換するために使用されます
@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "items") val items: List<GitHubRepo>
)
// GithubRepo class は APIから受け取ったユーザーのJSONを Kotlin オブジェクトに変換するために使用されます
@JsonClass(generateAdapter = true)
data class GitHubRepo(
    @Json(name = "id") val id: Long,// id: Long はユーザーの ID を表します
    @Json(name = "name") val name: String,// name: String はユーザーの名前を表します
    @Json(name = "full_name") val fullName: String,// full_name: String はユーザーのフルネームを表します
    @Json(name = "owner") val owner: Owner,// owner: Owner はユーザーの情報を表します
    @Json(name = "description") val description: String?,// description: String はユーザーの説明を表します
    @Json(name = "language") val language: String?,// language: String はユーザーの言語を表します
    @Json(name = "stargazers_count") val stargazersCount: Int,// stargazers_count: Int はユーザーのスター数を表します
    @Json(name = "watchers_count") val watchersCount: Int,// watchers_count: Int はユーザーのウォッチ数を表します
    @Json(name = "forks_count") val forksCount: Int,// forks_count: Int はユーザーのフォーク数を表します
    @Json(name = "open_issues_count") val openIssuesCount: Int// open_issues_count: Int はユーザーのオープンイシュー数を表します
)
// Owner class は APIから受け取ったユーザーのJSONを Kotlin オブジェクトに変換するために使用されます
@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "login") val login: String,// login: String はユーザーの名前を表します
    @Json(name = "avatar_url") val avatarUrl: String// avatar_url: String はユーザーのアバター画像の URL を表します
)
