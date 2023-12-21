package com.dzakdzaks.news.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dzakdzaks.news.domain.model.News
import com.dzakdzaks.news.ui.navArgs

class DetailViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val news = savedStateHandle.navArgs<News>()
}