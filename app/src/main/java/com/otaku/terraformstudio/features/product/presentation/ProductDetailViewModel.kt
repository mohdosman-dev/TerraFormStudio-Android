package com.otaku.terraformstudio.features.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.R
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
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
    private val productRepository: ProductRepository
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
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.ShowSnackbar(UiText.StringResource(R.string.added_to_cart)))
                }
            }
            is ProductDetailAction.OnRelatedProductClick -> {
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.NavigateToProduct(action.slug))
                }
            }
            ProductDetailAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(ProductDetailEvent.NavigateBack)
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
