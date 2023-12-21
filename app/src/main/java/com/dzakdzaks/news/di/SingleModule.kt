package com.dzakdzaks.news.di

import com.dzakdzaks.news.data.NewsRepositoryImpl
import com.dzakdzaks.news.domain.NewsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

fun singleModule() = module {
    single<HttpClient> {
        HttpClient(Android) {
            expectSuccess = true

            install(DefaultRequest) {
                headers.append("Content-Type", "application/json")
            }

            install(HttpTimeout) {
                requestTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
                socketTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
                connectTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<NewsRepository> {
        NewsRepositoryImpl(get())
    }
}