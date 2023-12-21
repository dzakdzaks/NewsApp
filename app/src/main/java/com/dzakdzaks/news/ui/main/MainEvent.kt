package com.dzakdzaks.news.ui.main

sealed class MainEvent {
    data object OnFetchNews: MainEvent()
}