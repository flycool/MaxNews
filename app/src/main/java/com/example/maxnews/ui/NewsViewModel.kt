package com.example.maxnews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maxnews.data.model.Article
import com.example.maxnews.data.model.NewsResponse
import com.example.maxnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewState())
    val state = _state.asStateFlow()

    var pageNumber = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        updateState(isLoading = true)
        try {
            val response = newsRepository.getBreakingNews(countryCode, pageNumber)
            handleBreakingNewsResponse(response)
        } catch (e: Exception) {
            updateState(
                isLoading = false,
                errorMessage = e.message
            )
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                updateState(
                    articles = result.articles,
                    isLoading = false
                )
                return
            }
        }
        updateState(
            errorMessage = response.message(),
            isLoading = false,
        )
    }

    private fun updateState(
        articles: List<Article> = emptyList(),
        errorMessage: String? = null,
        isLoading: Boolean = false
    ) {
        _state.update {
            it.copy(
                articles = articles,
                error = errorMessage,
                isLoading = isLoading
            )
        }
    }
}