package com.otaku.terraformstudio.features.home.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.home.domain.HomeConfiguration

data class HomeState(
    val homeConfiguration: HomeConfiguration? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface HomeAction {
    data object OnRefresh : HomeAction
    data class OnProductClick(val slug: String) : HomeAction
    data class OnArtisanClick(val artisanId: String) : HomeAction
    data class OnCollectionClick(val collectionId: String) : HomeAction
    data class OnCtaClick(val label: String, val target: String) : HomeAction
}

sealed interface HomeEvent {
    data class NavigateToProduct(val slug: String) : HomeEvent
    data class NavigateToArtisan(val artisanId: String) : HomeEvent
    data class NavigateToCollection(val collectionId: String) : HomeEvent
    data class ShowError(val message: UiText) : HomeEvent
}
