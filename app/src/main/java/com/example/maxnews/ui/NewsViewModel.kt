package com.example.maxnews.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxnews.data.model.Article
import com.example.maxnews.data.model.NewsResponse
import com.example.maxnews.repository.NewsRepository
import com.example.maxnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _breakingNewsState = MutableStateFlow(NewState())
    val state = _breakingNewsState.asStateFlow()
    var pageNumber = 1

    var searchPageNumber = 1

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearQuery(input: String) {
        searchQuery = input
        println("search query: $searchQuery")
    }

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        updateState(
            state = _breakingNewsState,
            isLoading = true
        )
        try {
            val response = newsRepository.getBreakingNews(countryCode, pageNumber)
            handleBreakingNewsResponse(response)
        } catch (e: Exception) {
            updateState(
                state = _breakingNewsState,
                isLoading = false,
                errorMessage = e.message
            )
        }
    }

    val searchState: StateFlow<NewState> =
        snapshotFlow { searchQuery }
            .filter { it.isNotEmpty() }
            .mapLatest {
                newsRepository.searchNews(it, searchPageNumber)
            }
            .map {
                handleSearchNewsResponse(it)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = NewState()
            )

    private fun handleSearchNewsResponse(resource: Resource<NewsResponse>): NewState {
        return when (resource) {
            is Resource.Success -> {
                NewState(
                    articles = resource.data!!.articles,
                    isLoading = false,
                )
            }
            is Resource.Error -> {
                val isFlowCancelled = resource.message!!.contains("cancelled")
                NewState(
                    isLoading = isFlowCancelled,
                    error = if (isFlowCancelled) null else resource.message
                )
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                updateState(
                    state = _breakingNewsState,
                    articles = result.articles,
                    isLoading = false
                )
                return
            }
        }
        updateState(
            state = _breakingNewsState,
            errorMessage = response.message(),
            isLoading = false,
        )
    }

    private fun updateState(
        state: MutableStateFlow<NewState>,
        articles: List<Article> = emptyList(),
        errorMessage: String? = null,
        isLoading: Boolean = false
    ) {
        state.update {
            it.copy(
                articles = articles,
                error = errorMessage,
                isLoading = isLoading
            )
        }
    }
}