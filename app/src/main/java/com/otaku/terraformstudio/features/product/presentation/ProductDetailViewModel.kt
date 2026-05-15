package com.otaku.terraformstudio.features.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.cart.domain.CartRepository
import com.otaku.terraformstudio.features.home.presentation.toUiText
import com.otaku.terraformstudio.features.product.domain.ProductRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val slug: String,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    private val _events = Channel<ProductDetailEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProduct()
    }

    fun onAction(action: ProductDetailAction) {
        when (action) {
            ProductDetailAction.OnRefresh -> loadProduct()
            ProductDetailAction.OnAddToCart -> {
                val productId = _state.value.product?.id ?: return
                viewModelScope.launch {
                    cartRepository.addItem(productId, 1)
                        .onSuccess {
                            _events.send(ProductDetailEvent.ShowSnackbar(UiText.DynamicString("Added to bag")))
                        }
                        .onFailure {
                            _events.send(ProductDetailEvent.ShowSnackbar(UiText.DynamicString("Failed to add to bag")))
                        }
                }
            }

            is ProductDetailAction.OnRelatedProductClick -> {
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.NavigateToProduct(action.slug))
                }
            }

            is ProductDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.NavigateBack)
                }
            }

            is ProductDetailAction.OnArtisanCardClicked -> {
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.NavigateToArtisan(action.slug))
                }
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            productRepository.getProductDetail(slug)
                .onSuccess { product ->
                    _state.update { it.copy(product = product, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }
}
