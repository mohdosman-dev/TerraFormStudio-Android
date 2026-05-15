package com.otaku.terraformstudio.features.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.features.cart.domain.CartRepository
import com.otaku.terraformstudio.features.home.presentation.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _events = Channel<CartEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadCart()
    }

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.OnRefresh -> loadCart()
            is CartAction.OnIncreaseQuantity -> updateQuantity(action.productId, +1)
            is CartAction.OnDecreaseQuantity -> updateQuantity(action.productId, -1)
            is CartAction.OnRemoveItem -> removeItem(action.productId)
            CartAction.OnProceedToCheckout -> {
                viewModelScope.launch { _events.send(CartEvent.NavigateToCheckout) }
            }
            CartAction.OnContinueShopping -> {
                viewModelScope.launch { _events.send(CartEvent.NavigateToDiscover) }
            }
            CartAction.OnBackClick -> {
                viewModelScope.launch { _events.send(CartEvent.NavigateBack) }
            }
            is CartAction.OnProductClick -> {
                viewModelScope.launch { _events.send(CartEvent.NavigateToProduct(action.slug)) }
            }
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            cartRepository.getCart()
                .onSuccess { cart ->
                    _state.update { it.copy(cart = cart, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun updateQuantity(productId: String, delta: Int) {
        val current = _state.value.cart ?: return
        val item = current.items.find { it.productId == productId } ?: return
        val newQty = item.quantity + delta

        if (newQty <= 0) {
            removeItem(productId)
            return
        }

        _state.update { it.copy(updatingItemIds = it.updatingItemIds + productId) }
        viewModelScope.launch {
            cartRepository.updateItemQuantity(productId, newQty)
                .onSuccess { cart ->
                    _state.update { it.copy(cart = cart, updatingItemIds = it.updatingItemIds - productId) }
                }
                .onFailure {
                    _state.update { it.copy(updatingItemIds = it.updatingItemIds - productId) }
                    loadCart()
                }
        }
    }

    private fun removeItem(productId: String) {
        _state.update { it.copy(updatingItemIds = it.updatingItemIds + productId) }
        viewModelScope.launch {
            cartRepository.removeItem(productId)
                .onSuccess { cart ->
                    _state.update { it.copy(cart = cart, updatingItemIds = it.updatingItemIds - productId) }
                }
                .onFailure {
                    _state.update { it.copy(updatingItemIds = it.updatingItemIds - productId) }
                    loadCart()
                }
        }
    }
}