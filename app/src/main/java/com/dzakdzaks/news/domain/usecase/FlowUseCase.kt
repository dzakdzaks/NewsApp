package com.dzakdzaks.news.domain.usecase

import com.dzakdzaks.news.BuildConfig
import com.dzakdzaks.news.data.model.ErrorResponse
import com.dzakdzaks.news.data.model.NewsResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    operator fun invoke(parameters: P): Flow<NewsResult<R>> = execute(parameters)
        .catch { e ->
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }

            when (e) {
                is ResponseException -> {
                    val errorResponse = e.response.body<ErrorResponse>()
                    emit(NewsResult.Error(errorResponse.message.orEmpty()))
                }

                else -> {
                    emit(NewsResult.Error(e.message.orEmpty()))
                }
            }
        }
        .onStart {
            emit(NewsResult.Loading)
        }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<NewsResult<R>>
}