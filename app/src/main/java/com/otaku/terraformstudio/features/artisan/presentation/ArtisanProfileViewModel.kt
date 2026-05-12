package com.otaku.terraformstudio.features.artisan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.artisan.domain.ArtisanRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtisanProfileViewModel(
    private val slug: String,
    private val repository: ArtisanRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ArtisanProfileState())
    val state = _state.asStateFlow()

    private val _events = Channel<ArtisanProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onAction(action: ArtisanProfileAction) {
        when (action) {
            ArtisanProfileAction.OnRefresh -> loadProfile()
            ArtisanProfileAction.OnBackClick -> {
                viewModelScope.launch { _events.send(ArtisanProfileEvent.NavigateBack) }
            }
            is ArtisanProfileAction.OnProductClick -> {
                viewModelScope.launch { _events.send(ArtisanProfileEvent.NavigateToProduct(action.slug)) }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getArtisanProfile(slug)
                .onSuccess { profile ->
                    _state.update { it.copy(profile = profile, isLoading = false) }
                }
                .onFailure { error ->
                    val uiText = when (error) {
                        com.otaku.terraformstudio.core.domain.DataError.Network.NO_INTERNET ->
                            UiText.DynamicString("No internet connection")
                        com.otaku.terraformstudio.core.domain.DataError.Network.SERVER_ERROR ->
                            UiText.DynamicString("Server error")
                        com.otaku.terraformstudio.core.domain.DataError.Network.NOT_FOUND ->
                            UiText.DynamicString("Artisan not found")
                        else -> UiText.DynamicString("Something went wrong")
                    }
                    _state.update { it.copy(isLoading = false, error = uiText) }
                }
        }
    }
}
