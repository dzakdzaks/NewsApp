package com.dzakdzaks.news.di

import com.dzakdzaks.news.domain.usecase.FetchNewsUseCase
import org.koin.dsl.module

fun factoryModule() = module {
    factory<FetchNewsUseCase> {
        FetchNewsUseCase(get())
    }
}