package com.example.maxnews.repository

import com.example.maxnews.api.NewsApi
import com.example.maxnews.data.local.ArticleDatabase
import com.example.maxnews.data.model.NewsResponse
import com.example.maxnews.util.Resource
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val db: ArticleDatabase,
    private val api: NewsApi
) {

    suspend fun getBreakingNews(country: String, pageNumber: Int) =
        api.getBreakingNews(country, pageNumber)

    suspend fun searchNews(search: String, page: Int): Resource<NewsResponse> {
        return try {
            val response = api.searchForNews(search, page)
            if (response.isSuccessful) {
               response.body()?.let { result->
                   Resource.Success(result)
               } ?: Resource.Error()
            } else {
                 Resource.Error(response.message())
            }
        } catch (e: Exception) {
            if (e is SocketTimeoutException) {
                Resource.Error(e.message)
            } else {
                Resource.Error(e.message)
            }
        }
    }


}