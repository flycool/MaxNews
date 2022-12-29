package com.example.maxnews.di

import com.example.maxnews.api.NewsApi
import com.example.maxnews.data.local.ArticleDatabase
import com.example.maxnews.repository.NewsRepository
import com.example.maxnews.util.base_url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofitApi(client: OkHttpClient): NewsApi =
        Retrofit.Builder()
            .baseUrl(base_url)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()

    @Provides
    @Singleton
    fun provideNewsRepository(
        db: ArticleDatabase,
        api: NewsApi
    ): NewsRepository = NewsRepository(db, api)

}