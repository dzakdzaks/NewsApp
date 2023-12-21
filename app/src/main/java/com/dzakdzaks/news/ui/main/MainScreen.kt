@file:OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)

package com.dzakdzaks.news.ui.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dzakdzaks.news.R
import com.dzakdzaks.news.domain.model.News
import com.dzakdzaks.news.ext.formatDate
import com.dzakdzaks.news.ext.isEmptyOrBlank
import com.dzakdzaks.news.ext.orEmpty
import com.dzakdzaks.news.ui.destinations.DetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    mainViewModel: MainViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },

        ) {
        val mainState by mainViewModel.mainState.collectAsStateWithLifecycle()
        Content(
            paddingValues = it,
            mainState = mainState,
            onEvent = mainViewModel::onEvent,
            onClick = { news ->
                navigator.navigate(DetailScreenDestination(news))
            }
        )
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    mainState: MainState,
    onEvent: (MainEvent) -> Unit,
    onClick: (News) -> Unit
) {
    val context = LocalContext.current
    val refreshing = mainState is MainState.Loading
    val state = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            onEvent(MainEvent.OnFetchNews)
        }
    )

    Box(
        Modifier
            .padding(paddingValues)
            .pullRefresh(state)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            when (mainState) {
                MainState.Loading -> Unit
                is MainState.Error -> {
                    item {
                        Text(modifier = Modifier.padding(16.dp), text = mainState.message)
                    }
                }

                is MainState.Success -> {
                    items(mainState.data) {
                        CardItem(
                            item = it,
                            context = context,
                            onClick = onClick
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun CardItem(
    item: News,
    context: Context,
    onClick: (News) -> Unit
) {
    Card(onClick = { onClick(item) }) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(item.urlToImage.orEmpty("https://picsum.photos/1280/720"))
                    .crossfade(true)
                    .placeholder(R.drawable.ic_launcher_background)
                    .build(),
                contentScale = ContentScale.FillBounds
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = stringResource(R.string.app_name)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    text = item.source,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = item.title,
                style = MaterialTheme.typography.labelLarge
            )
            if (item.description.isEmptyOrBlank().not()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val footer = if (item.author.isEmptyOrBlank()) {
                item.publishedAt.formatDate()
            } else {
                "${item.publishedAt.formatDate()} by ${item.author}"
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = footer,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}