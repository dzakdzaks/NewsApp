package com.dzakdzaks.news.ui.main

import com.dzakdzaks.news.domain.model.News

sealed class MainState {
    data object Loading : MainState()
    data class Error(val message: String) : MainState()
    data class Success(val data: List<News>) : MainState()
}