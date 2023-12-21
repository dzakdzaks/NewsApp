package com.dzakdzaks.news

import android.app.Application
import com.dzakdzaks.news.di.factoryModule
import com.dzakdzaks.news.di.singleModule
import com.dzakdzaks.news.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NewsApplication)
            modules(
                singleModule(),
                factoryModule(),
                viewModelModule()
            )
        }
    }
}