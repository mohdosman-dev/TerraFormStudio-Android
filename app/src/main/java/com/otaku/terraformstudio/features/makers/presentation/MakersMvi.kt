package com.otaku.terraformstudio.features.makers.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.makers.domain.MakersArtisan

data class MakersState(
    val artisans: List<MakersArtisan> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: UiText? = null,
    val page: Int = 1,
    val totalPages: Int = 1,
    val total: Int = 0
)

sealed interface MakersAction {
    data object OnRefresh : MakersAction
    data object OnLoadMore : MakersAction
    data class OnArtisanClick(val slug: String) : MakersAction
}

sealed interface MakersEvent {
    data class NavigateToArtisan(val slug: String) : MakersEvent
}
