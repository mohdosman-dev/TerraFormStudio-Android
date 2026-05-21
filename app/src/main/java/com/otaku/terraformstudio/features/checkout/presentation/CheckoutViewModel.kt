package com.otaku.terraformstudio.features.checkout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.checkout.domain.CheckoutRepository
import com.otaku.terraformstudio.features.checkout.domain.ShippingAddress
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val checkoutRepository: CheckoutRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state = _state.asStateFlow()

    private val _events = Channel<CheckoutEvent>()
    val events = _events.receiveAsFlow()

    fun initialize(cartId: String) {
        loadDeliveryMethods()
        initCheckout(cartId)
    }

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnShippingAddressChange -> _state.update {
                it.copy(shippingAddress = action.address, addressError = null)
            }
            is CheckoutAction.OnDeliveryMethodSelect -> _state.update {
                it.copy(selectedDeliveryMethodId = action.methodId)
            }
            is CheckoutAction.OnCustomerNoteChange -> _state.update {
                it.copy(customerNote = action.note)
            }
            CheckoutAction.OnContinueFromShipping -> validateAndProceedToPayment()
            is CheckoutAction.OnCardNumberChange -> {
                val cleaned = action.value.filter { it.isDigit() }.take(16)
                _state.update { it.copy(cardNumber = cleaned) }
            }
            is CheckoutAction.OnCardExpiryChange -> {
                val cleaned = action.value.filter { it.isDigit() }.take(4)
                val formatted = if (cleaned.length >= 3) {
                    "${cleaned.take(2)}/${cleaned.drop(2)}"
                } else cleaned
                _state.update { it.copy(cardExpiry = formatted) }
            }
            is CheckoutAction.OnCardCvvChange -> {
                _state.update { it.copy(cardCvv = action.value.take(4)) }
            }
            CheckoutAction.OnContinueFromPayment -> _state.update {
                it.copy(currentStep = CheckoutStep.REVIEW)
            }
            CheckoutAction.OnPlaceOrder -> placeOrder()
            CheckoutAction.OnBackToShipping -> _state.update {
                it.copy(currentStep = CheckoutStep.SHIPPING)
            }
            CheckoutAction.OnBackToPayment -> _state.update {
                it.copy(currentStep = CheckoutStep.PAYMENT)
            }
            CheckoutAction.OnBackToCart -> {
                _events.trySend(CheckoutEvent.NavigateBack)
            }
        }
    }

    private fun loadDeliveryMethods() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkoutRepository.getDeliveryMethods()
                .onSuccess { methods ->
                    val active = methods.filter { it.isActive }
                    val defaultId = active.find { it.isDefault }?.id ?: active.firstOrNull()?.id
                    _state.update {
                        it.copy(
                            deliveryMethods = active,
                            selectedDeliveryMethodId = defaultId,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                    _events.send(CheckoutEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun initCheckout(cartId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkoutRepository.initCheckout(cartId)
                .onSuccess { session ->
                    _state.update {
                        it.copy(
                            session = session,
                            isLoading = false,
                            shippingAddress = session.shippingAddress ?: ShippingAddress(),
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                    _events.send(CheckoutEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun validateAndProceedToPayment() {
        val address = _state.value.shippingAddress
        val methodId = _state.value.selectedDeliveryMethodId
        val session = _state.value.session ?: return

        if (address.fullName.isBlank() || address.addressLine1.isBlank() || address.city.isBlank() || methodId == null) {
            _state.update { it.copy(addressError = "Please fill in all required fields") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkoutRepository.updateShipping(session.id, address, methodId)
                .onSuccess { updatedSession ->
                    _state.update {
                        it.copy(
                            session = updatedSession,
                            currentStep = CheckoutStep.PAYMENT,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(CheckoutEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun placeOrder() {
        val session = _state.value.session ?: return

        viewModelScope.launch {
            _state.update { it.copy(isPlacingOrder = true) }
            checkoutRepository.completeCheckout(session.id)
                .onSuccess { confirmation ->
                    _state.update { it.copy(isPlacingOrder = false, orderConfirmation = confirmation) }
                    _events.send(CheckoutEvent.OrderPlaced(orderId = confirmation.orderId, grandTotal = confirmation.grandTotal))
                }
                .onFailure { error ->
                    _state.update { it.copy(isPlacingOrder = false) }
                    _events.send(CheckoutEvent.ShowError(error.toUiText()))
                }
        }
    }
}

private fun com.otaku.terraformstudio.core.domain.DataError.Network.toUiText() =
    UiText.DynamicString(when (this) {
        com.otaku.terraformstudio.core.domain.DataError.Network.NO_INTERNET -> "No internet connection"
        com.otaku.terraformstudio.core.domain.DataError.Network.SERVER_ERROR -> "Server error. Please try again."
        com.otaku.terraformstudio.core.domain.DataError.Network.NOT_FOUND -> "Checkout session not found"
        com.otaku.terraformstudio.core.domain.DataError.Network.CONFLICT -> "Cart has been modified. Please refresh."
        com.otaku.terraformstudio.core.domain.DataError.Network.BAD_REQUEST -> "Invalid request. Please check your information."
        else -> "Something went wrong. Please try again."
    })
