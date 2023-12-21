@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.dzakdzaks.news.ui.detail

import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dzakdzaks.news.R
import com.dzakdzaks.news.domain.model.News
import com.dzakdzaks.news.ext.share
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination(
    navArgsDelegate = News::class
)
@Composable
fun DetailScreen(
    navigator: DestinationsNavigator,
    detailViewModel: DetailViewModel = koinViewModel()
) {
    val context = LocalContext.current

    var webView: WebView? = null

    var isWebViewCanGoBack by remember { mutableStateOf(false) }

    var isPageFinishedLoad by remember { mutableStateOf(false) }

    var loadingProgress by remember { mutableIntStateOf(0) }

    BackHandler(enabled = true) {
        onBackPressed(
            navigator = navigator,
            isWebViewCanGoBack = isWebViewCanGoBack,
            webView = webView
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.news_detail),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed(
                                navigator = navigator,
                                isWebViewCanGoBack = isWebViewCanGoBack,
                                webView = webView
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { webView?.reload() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(id = R.string.refresh)
                        )
                    }
                    IconButton(onClick = { context.share(detailViewModel.news) }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(id = R.string.share)
                        )
                    }
                }
            )
        },

        ) {
        Content(
            paddingValues = it,
            news = detailViewModel.news,
            isPageFinishedLoad = isPageFinishedLoad,
            loadingProgress = loadingProgress,
            onSetWebView = { web ->
                webView = web
            },
            onSetWebViewCanGoBack = { canGoBack ->
                isWebViewCanGoBack = canGoBack
            },
            onProgressChanged = { progress ->
                loadingProgress = progress
            },
            onPageFinished = { finished ->
                isPageFinishedLoad = finished
                loadingProgress = 0
            },
        )
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    news: News,
    isPageFinishedLoad: Boolean,
    loadingProgress: Int,
    onSetWebView: (WebView) -> Unit,
    onSetWebViewCanGoBack: (Boolean) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onPageFinished: (Boolean) -> Unit,
) {
    Box(
        Modifier.padding(paddingValues)
    ) {
        WebView(
            modifier = Modifier.fillMaxSize(),
            onSetWebView = onSetWebView,
            url = news.url,
            onSetWebViewCanGoBack = onSetWebViewCanGoBack,
            onProgressChanged = onProgressChanged,
            onPageFinished = onPageFinished,
        )

        if (isPageFinishedLoad.not()) {
            LinearProgressIndicator(
                progress = loadingProgress.toFloat(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
            )
        }
    }
}

private fun onBackPressed(
    navigator: DestinationsNavigator,
    isWebViewCanGoBack: Boolean,
    webView: WebView?
) {
    if (isWebViewCanGoBack) {
        webView?.goBack()
    } else {
        navigator.popBackStack()
    }
}