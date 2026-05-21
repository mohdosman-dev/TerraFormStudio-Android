package com.otaku.terraformstudio.features.checkout.presentation

import com.otaku.terraformstudio.core.presentation.UiText
import com.otaku.terraformstudio.features.checkout.domain.CheckoutItem
import com.otaku.terraformstudio.features.checkout.domain.CheckoutSession
import com.otaku.terraformstudio.features.checkout.domain.DeliveryMethod
import com.otaku.terraformstudio.features.checkout.domain.OrderConfirmation
import com.otaku.terraformstudio.features.checkout.domain.ShippingAddress

enum class CheckoutStep { SHIPPING, PAYMENT, REVIEW }

data class CheckoutState(
    val currentStep: CheckoutStep = CheckoutStep.SHIPPING,
    val deliveryMethods: List<DeliveryMethod> = emptyList(),
    val selectedDeliveryMethodId: String? = null,
    val shippingAddress: ShippingAddress = ShippingAddress(),
    val customerNote: String = "",
    val session: CheckoutSession? = null,
    val cardNumber: String = "",
    val cardExpiry: String = "",
    val cardCvv: String = "",
    val isLoading: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val addressError: String? = null,
    val error: UiText? = null,
    val orderConfirmation: OrderConfirmation? = null,
    val items: List<CheckoutItem> = emptyList(),
)

sealed interface CheckoutAction {
    data class OnShippingAddressChange(val address: ShippingAddress) : CheckoutAction
    data class OnDeliveryMethodSelect(val methodId: String) : CheckoutAction
    data class OnCustomerNoteChange(val note: String) : CheckoutAction
    data object OnContinueFromShipping : CheckoutAction
    data class OnCardNumberChange(val value: String) : CheckoutAction
    data class OnCardExpiryChange(val value: String) : CheckoutAction
    data class OnCardCvvChange(val value: String) : CheckoutAction
    data object OnContinueFromPayment : CheckoutAction
    data object OnPlaceOrder : CheckoutAction
    data object OnBackToShipping : CheckoutAction
    data object OnBackToPayment : CheckoutAction
    data object OnBackToCart : CheckoutAction
}

sealed interface CheckoutEvent {
    data class ShowError(val error: UiText) : CheckoutEvent
    data object NavigateBack : CheckoutEvent
    data class OrderPlaced(val orderId: String, val grandTotal: Double) : CheckoutEvent
}
