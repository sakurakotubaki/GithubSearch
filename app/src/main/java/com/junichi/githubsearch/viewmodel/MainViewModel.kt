package com.junichi.githubsearch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junichi.githubsearch.BuildConfig
import com.junichi.githubsearch.api.GitHubApi
import com.junichi.githubsearch.data.GitHubRepo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// MainViewModelは、GitHub API を呼び出すための ViewModel です
// ViewModel は、UI と Model の間を結びつける役割を担います
class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        // GitHubのPersonal Access Token
        private val GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN.also {
            Log.d(TAG, "Token loaded: ${it.take(10)}...")
        }
    }

    // UiState は、UI に表示するためのデータクラス
    data class UiState(
        val repositories: List<GitHubRepo> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    // UiState を保持する変数
    private val _uiState = MutableStateFlow(UiState())

    // UiState を読み取るためのプロパティ
    val uiState: StateFlow<UiState> = _uiState

    // Moshi を使って JSON を Kotlin オブジェクトに変換するための変数
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // OkHttpClient を使って GitHub API を呼び出すための変数
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "token $GITHUB_TOKEN")
                .header("Accept", "application/vnd.github.v3+json")
                .method(original.method, original.body)
                .build()
            Log.d(TAG, "Making request to: ${request.url}")
            Log.d(TAG, "Authorization header: token ${GITHUB_TOKEN.take(10)}...")
            chain.proceed(request)
        }
        .build()

    // Retrofit を使って GitHub API を呼び出すための変数
    private val gitHubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(GitHubApi::class.java)

    // searchRepositories は、GitHub API を呼び出すためのメソッド
    fun searchRepositories(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(repositories = emptyList(), error = null) }
            return
        }

        // コルーチンを使って GitHub API を呼び出す
        viewModelScope.launch {
            try {
                // UiState を更新
                _uiState.update { it.copy(isLoading = true, error = null) }
                Log.d(TAG, "Searching repositories with query: $query")

                // GitHub API を呼び出す
                val response = gitHubApi.searchRepositories(query)
                Log.d(TAG, "Received ${response.items.size} repositories")
                response.items.forEach { repo ->
                    Log.d(TAG, "Repo: ${repo.fullName}, Avatar: ${repo.owner.avatarUrl}")
                }

                // UiState を更新
                _uiState.update { it.copy(
                    repositories = response.items,
                    isLoading = false,
                    error = null
                ) }

                // 例外発生時の処理
            } catch (e: Exception) {
                // UiState を更新
                Log.e(TAG, "Error searching repositories", e)
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }
}
