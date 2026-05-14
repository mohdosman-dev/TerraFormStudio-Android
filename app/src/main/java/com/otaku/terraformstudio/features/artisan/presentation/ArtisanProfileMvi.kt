package com.otaku.terraformstudio.features.artisan.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProfile

data class ArtisanProfileState(
    val profile: ArtisanProfile? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface ArtisanProfileAction {
    data object OnRefresh : ArtisanProfileAction
    data object OnBackClick : ArtisanProfileAction
    data class OnProductClick(val slug: String) : ArtisanProfileAction
}

sealed interface ArtisanProfileEvent {
    data object NavigateBack : ArtisanProfileEvent
    data class NavigateToProduct(val slug: String) : ArtisanProfileEvent
}
