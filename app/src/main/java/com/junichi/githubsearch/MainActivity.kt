package com.junichi.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.junichi.githubsearch.ui.theme.GithubSearchTheme
import com.junichi.githubsearch.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GitHubSearchScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubSearchScreen(
    viewModel: MainViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.searchRepositories(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search GitHub repositories...") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn {
                    items(uiState.repositories) { repo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Avatar image
                                AsyncImage(
                                    model = repo.owner.avatarUrl,
                                    contentDescription = "Avatar for ${repo.owner.login}",
                                    modifier = Modifier
                                        .size(48.dp),
                                    contentScale = ContentScale.Crop
                                )
                                
                                // Repository details
                                Column {
                                    Text(
                                        text = repo.fullName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    repo.description?.let { description ->
                                        Text(
                                            text = description,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text("⭐ ${repo.stargazersCount}")
                                        Text("👀 ${repo.watchersCount}")
                                        Text("🔨 ${repo.forksCount}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}