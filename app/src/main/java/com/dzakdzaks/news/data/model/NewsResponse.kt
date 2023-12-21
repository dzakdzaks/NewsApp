package com.dzakdzaks.news.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("articles")
    val articles: List<Article>? = null
) {
    @Serializable
    data class Article(
        @SerialName("source")
        val source: Source? = null,
        @SerialName("author")
        val author: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("urlToImage")
        val urlToImage: String? = null,
        @SerialName("publishedAt")
        val publishedAt: String? = null,
        @SerialName("content")
        val content: String? = null,
    )

    @Serializable
    data class Source(
        @SerialName("name")
        val name: String? = null
    )
}
