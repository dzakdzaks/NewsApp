package com.dzakdzaks.news.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzakdzaks.news.data.model.NewsResult
import com.dzakdzaks.news.domain.usecase.FetchNewsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val fetchNewsUseCase: FetchNewsUseCase
) : ViewModel() {

    var job: Job? = null

    private val _mainState: MutableStateFlow<MainState> =
        MutableStateFlow(MainState.Loading)
    val mainState: StateFlow<MainState> = _mainState.asStateFlow()

    init {
        onEvent(MainEvent.OnFetchNews)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.OnFetchNews -> fetchNews()
        }
    }

    private fun fetchNews() {
        job?.cancel()
        job = viewModelScope.launch {
            fetchNewsUseCase.invoke(Unit).collect { result ->
                when (result) {
                    NewsResult.Loading -> _mainState.update { MainState.Loading }
                    is NewsResult.Error -> _mainState.update { MainState.Error(result.message) }
                    is NewsResult.Success -> _mainState.update { MainState.Success(result.data) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}
