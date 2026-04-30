package com.otaku.terraformstudio.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.R
import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.home.domain.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadHomeConfiguration()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> loadHomeConfiguration()
            is HomeAction.OnProductClick -> {
                viewModelScope.launch { _events.send(HomeEvent.NavigateToProduct(action.productId)) }
            }
            is HomeAction.OnArtisanClick -> {
                viewModelScope.launch { _events.send(HomeEvent.NavigateToArtisan(action.artisanId)) }
            }
            is HomeAction.OnCollectionClick -> {
                viewModelScope.launch { _events.send(HomeEvent.NavigateToCollection(action.collectionId)) }
            }
            is HomeAction.OnCtaClick -> {
                // Handle CTA based on target if needed, or generic navigation
            }
        }
    }

    private fun loadHomeConfiguration() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            homeRepository.getHomeConfiguration()
                .onSuccess { config ->
                    _state.update { it.copy(homeConfiguration = config, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                    _events.send(HomeEvent.ShowError(error.toUiText()))
                }
        }
    }
}

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
        DataError.Network.UNAUTHORIZED -> UiText.StringResource(R.string.error_unauthorized)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}
