package com.dzakdzaks.news.domain.usecase

import com.dzakdzaks.news.data.model.NewsResult
import com.dzakdzaks.news.domain.NewsRepository
import com.dzakdzaks.news.domain.model.News
import com.dzakdzaks.news.domain.model.toNews
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FetchNewsUseCase(
    private val newsRepository: NewsRepository
) : FlowUseCase<Unit, List<News>>() {
    override fun execute(parameters: Unit): Flow<NewsResult<List<News>>> {
        return flow {
            emit(NewsResult.Success(newsRepository.getNews().toNews()))
        }
    }
}