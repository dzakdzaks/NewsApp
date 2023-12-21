package com.dzakdzaks.news.domain

import com.dzakdzaks.news.data.model.NewsResponse

interface NewsRepository {
    suspend fun getNews(): NewsResponse
}