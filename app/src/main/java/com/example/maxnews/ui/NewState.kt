package com.example.maxnews.ui

import com.example.maxnews.data.model.Article

data class NewState(
    val articles: List<Article> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val messageEvent: List<String> = emptyList()
)
