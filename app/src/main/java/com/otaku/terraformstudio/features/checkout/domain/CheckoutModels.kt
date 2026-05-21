package com.otaku.terraformstudio.features.checkout.domain

data class ShippingAddress(
    val fullName: String = "",
    val phone: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",
    val country: String = "United States",
)

data class CheckoutSession(
    val id: String,
    val status: String,
    val shippingAddress: ShippingAddress?,
    val deliveryOption: DeliveryOption?,
    val priceSummary: PriceSummary,
)

data class DeliveryOption(
    val method: String,
    val price: Double,
    val estimatedDays: String,
)

data class PriceSummary(
    val subtotal: Double = 0.0,
    val shipping: Double = 0.0,
    val tax: Double = 0.0,
    val grandTotal: Double = 0.0,
)

data class CheckoutItem(
    val productId: String = "",
    val slug: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val variant: String = "",
    val tags: List<String> = emptyList(),
)

data class OrderConfirmation(
    val orderId: String,
    val status: String,
    val grandTotal: Double,
    val orderNumber: String = "",
    val customerName: String = "",
    val shippingAddress: ShippingAddress? = null,
    val items: List<CheckoutItem> = emptyList(),
    val expectedDeliveryStart: String = "",
    val expectedDeliveryEnd: String = "",
)
