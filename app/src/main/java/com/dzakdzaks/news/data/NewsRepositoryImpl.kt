package com.dzakdzaks.news.data

import com.dzakdzaks.news.BuildConfig
import com.dzakdzaks.news.data.model.NewsResponse
import com.dzakdzaks.news.domain.NewsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class NewsRepositoryImpl(
    private val client: HttpClient
) : NewsRepository {
    override suspend fun getNews(): NewsResponse {
        return client.get {
            url("${BuildConfig.BASE_URL}top-headlines")
            parameter("country", "us")
            parameter("category", "technology")
            parameter("apiKey", BuildConfig.API_KEY)
        }.body()
    }
}