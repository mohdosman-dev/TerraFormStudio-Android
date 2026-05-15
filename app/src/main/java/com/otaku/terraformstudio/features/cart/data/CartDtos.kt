package com.otaku.terraformstudio.features.cart.data

import com.otaku.terraformstudio.features.home.data.ProductDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
    @SerialName("_id")
    val id: String,
    val userId: String? = null,
    val guestId: String? = null,
    val status: String,
    val items: List<CartItemDto>,
    val totals: CartTotalsDto,
)

@Serializable
data class CartItemDto(
    @SerialName("productId")
    val product: ProductDto,
    val quantity: Int,
    val addedAt: String? = null,
)

@Serializable
data class CartTotalsDto(
    val subtotal: Double = 0.0,
    val estimatedShipping: Double = 0.0,
    val tax: Double = 0.0,
    val grandTotal: Double = 0.0,
)

@Serializable
data class AddToCartRequestDto(
    val productId: String,
    val quantity: Int = 1,
)

@Serializable
data class UpdateCartItemRequestDto(
    val quantity: Int,
)

@Serializable
data class MergeCartRequestDto(
    val guestId: String,
)