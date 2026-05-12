package com.otaku.terraformstudio.features.product.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.product.domain.ProductDetail

data class ProductDetailState(
    val product: ProductDetail? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface ProductDetailAction {
    data object OnRefresh : ProductDetailAction
    data object OnAddToCart : ProductDetailAction
    data class OnRelatedProductClick(val productId: String, val slug: String) : ProductDetailAction
    data class OnArtisanCardClicked(val slug: String) : ProductDetailAction
    data object OnBackClick : ProductDetailAction
}

sealed interface ProductDetailEvent {
    data class ShowSnackbar(val message: UiText) : ProductDetailEvent
    data class NavigateToProduct(val slug: String) : ProductDetailEvent
    data object NavigateBack : ProductDetailEvent
    data class NavigateToArtisan(val slug: String) : ProductDetailEvent
}
