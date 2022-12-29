package com.example.maxnews.repository

import com.example.maxnews.api.NewsApi
import com.example.maxnews.data.local.ArticleDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val db: ArticleDatabase,
    private val api: NewsApi
) {

    suspend fun getBreakingNews(country:String, pageNumber:Int) =
        api.getBreakingNews(country, pageNumber)

}