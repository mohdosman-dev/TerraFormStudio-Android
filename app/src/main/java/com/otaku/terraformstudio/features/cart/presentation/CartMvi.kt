package com.otaku.terraformstudio.features.cart.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.cart.domain.Cart

data class CartState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val updatingItemIds: Set<String> = emptySet(),
)

sealed interface CartAction {
    data object OnRefresh : CartAction
    data class OnIncreaseQuantity(val productId: String) : CartAction
    data class OnDecreaseQuantity(val productId: String) : CartAction
    data class OnRemoveItem(val productId: String) : CartAction
    data object OnProceedToCheckout : CartAction
    data object OnContinueShopping : CartAction
    data object OnBackClick : CartAction
    data class OnProductClick(val slug: String) : CartAction
}

sealed interface CartEvent {
    data object NavigateBack : CartEvent
    data object NavigateToCheckout : CartEvent
    data object NavigateToDiscover : CartEvent
    data class NavigateToProduct(val slug: String) : CartEvent
    data class ShowSnackbar(val message: UiText) : CartEvent
}