package com.dzakdzaks.news.data.model

sealed class NewsResult<out T> {
    data object Loading : NewsResult<Nothing>()
    data class Error(val message: String): NewsResult<Nothing>()
    data class Success<T>(val data: T): NewsResult<T>()
}