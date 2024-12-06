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

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    companion object {
        // GitHubのPersonal Access Token
        private val GITHUB_TOKEN = BuildConfig.GITHUB_TOKEN
    }

    data class UiState(
        val repositories: List<GitHubRepo> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "token $GITHUB_TOKEN")
                .header("Accept", "application/vnd.github.v3+json")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .build()

    private val gitHubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(GitHubApi::class.java)

    fun searchRepositories(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(repositories = emptyList(), error = null) }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                Log.d(TAG, "Searching repositories with query: $query")
                
                val response = gitHubApi.searchRepositories(query)
                Log.d(TAG, "Received ${response.items.size} repositories")
                response.items.forEach { repo ->
                    Log.d(TAG, "Repo: ${repo.fullName}, Avatar: ${repo.owner.avatarUrl}")
                }
                
                _uiState.update { it.copy(
                    repositories = response.items,
                    isLoading = false,
                    error = null
                ) }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching repositories", e)
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }
}
