package com.dzakdzaks.news.di

import com.dzakdzaks.news.ui.detail.DetailViewModel
import com.dzakdzaks.news.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelModule() = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::DetailViewModel)
}