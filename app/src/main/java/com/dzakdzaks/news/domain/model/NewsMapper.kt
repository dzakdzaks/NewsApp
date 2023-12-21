package com.dzakdzaks.news.domain.model

import com.dzakdzaks.news.data.model.NewsResponse

fun NewsResponse.toNews(): List<News> {
    return this.articles?.map {
        News(
            source = it.source?.name.orEmpty(),
            author = it.author.orEmpty(),
            title = it.title.orEmpty(),
            description = it.description.orEmpty(),
            url = it.url.orEmpty(),
            urlToImage = it.urlToImage.orEmpty(),
            publishedAt = it.publishedAt.orEmpty(),
            content = it.content.orEmpty()
        )
    } ?: emptyList()
}