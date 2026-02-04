package com.bobteachestech.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobteachestech.app.data.FeedItem
import com.bobteachestech.app.data.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: FeedRepository = FeedRepository()
) : ViewModel() {
    private val _postsState = MutableStateFlow(FeedUiState())
    val postsState: StateFlow<FeedUiState> = _postsState

    private val _videosState = MutableStateFlow(FeedUiState())
    val videosState: StateFlow<FeedUiState> = _videosState

    init {
        refreshPosts()
        refreshVideos()
    }

    fun refreshPosts() {
        _postsState.value = FeedUiState(isLoading = true)
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { repository.fetchPosts() }
            }.onSuccess { items ->
                _postsState.value = FeedUiState(items = items)
            }.onFailure { error ->
                _postsState.value = FeedUiState(errorMessage = error.message ?: "Unable to load posts")
            }
        }
    }

    fun refreshVideos() {
        _videosState.value = FeedUiState(isLoading = true)
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) { repository.fetchVideos() }
            }.onSuccess { items ->
                _videosState.value = FeedUiState(items = items)
            }.onFailure { error ->
                _videosState.value = FeedUiState(errorMessage = error.message ?: "Unable to load videos")
            }
        }
    }
}

data class FeedUiState(
    val items: List<FeedItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
