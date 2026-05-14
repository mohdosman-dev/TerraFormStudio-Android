package com.otaku.terraformstudio.features.makers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.makers.domain.MakersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MakersViewModel(
    private val repository: MakersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MakersState())
    val state = _state.asStateFlow()

    private val _events = Channel<MakersEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadArtisans()
    }

    fun onAction(action: MakersAction) {
        when (action) {
            MakersAction.OnRefresh -> loadArtisans()
            MakersAction.OnLoadMore -> loadNextPage()
            is MakersAction.OnArtisanClick -> {
                viewModelScope.launch { _events.send(MakersEvent.NavigateToArtisan(action.slug)) }
            }
        }
    }

    private fun loadArtisans() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, page = 1) }
            repository.getArtisans(page = 1)
                .onSuccess { list ->
                    _state.update {
                        it.copy(
                            artisans = list.artisans,
                            isLoading = false,
                            page = list.page,
                            totalPages = list.totalPages,
                            total = list.total
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun loadNextPage() {
        val current = _state.value
        if (current.isLoadingMore || current.page >= current.totalPages) return
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true) }
            repository.getArtisans(page = current.page + 1)
                .onSuccess { list ->
                    _state.update {
                        it.copy(
                            artisans = it.artisans + list.artisans,
                            isLoadingMore = false,
                            page = list.page,
                            totalPages = list.totalPages,
                            total = list.total
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoadingMore = false) }
                }
        }
    }

    private fun DataError.Network.toUiText(): UiText = when (this) {
        DataError.Network.NO_INTERNET -> UiText.DynamicString("No internet connection")
        DataError.Network.SERVER_ERROR -> UiText.DynamicString("Server error")
        else -> UiText.DynamicString("Something went wrong")
    }
}
