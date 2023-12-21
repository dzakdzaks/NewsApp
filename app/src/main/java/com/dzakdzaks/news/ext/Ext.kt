package com.dzakdzaks.news.ext

import android.content.Context
import android.content.Intent
import androidx.core.text.HtmlCompat
import com.dzakdzaks.news.domain.model.News
import java.text.SimpleDateFormat
import java.util.Locale

fun String?.orEmpty(default: String = ""): String {
    return if (this != null) {
        if (this.isNotEmpty() || this.isNotBlank()) {
            this
        } else {
            default
        }
    } else {
        default
    }
}

fun String.isEmptyOrBlank(): Boolean = this.isEmpty() || this.isBlank()

fun String.formatDate(): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())

        inputFormat.parse(this)?.let {
            return outputFormat.format(it)
        } ?: run {
            return "-"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return "-" // Handle the exception case
    }
}

fun Context.share(news: News) {
    val text = "${news.url}\n\n${news.title}"
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(shareIntent, "Share via")
    startActivity(chooser)
}