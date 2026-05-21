package com.otaku.terraformstudio.features.checkout.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryMethodResponseDto(
    val deliveryMethods: List<DeliveryMethodDto>
)

@Serializable
data class DeliveryMethodDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    val estimatedDays: String,
    val isActive: Boolean,
    val isDefault: Boolean,
)

@Serializable
data class InitCheckoutRequestDto(
    @SerialName("sessionId")
    val cartId: String,
)

@Serializable
data class CheckoutSessionDto(
    @SerialName("_id")
    val id: String,
    val status: String,
    val shippingAddress: ShippingAddressDto? = null,
    val deliveryOption: DeliveryOptionDto? = null,
    val priceValidation: PriceValidationDto,
)

@Serializable
data class ShippingAddressDto(
    val fullName: String = "",
    val phone: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = "UAE",
)

@Serializable
data class DeliveryOptionDto(
    val method: String,
    val price: Double,
    val estimatedDays: String,
)

@Serializable
data class PriceValidationDto(
    val subtotal: Double = 0.0,
    val shipping: Double = 0.0,
    val tax: Double = 0.0,
    val grandTotal: Double = 0.0,
)

@Serializable
data class UpdateShippingRequestDto(
    val shippingAddress: ShippingAddressDto,
    val deliveryOption: String,
)

@Serializable
data class CompleteCheckoutResponseDto(
    @SerialName("_id")
    val orderId: String,
    val status: String,
    val grandTotal: Double,
)
