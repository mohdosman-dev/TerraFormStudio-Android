package com.otaku.terraformstudio.features.cart.domain

data class Cart(
    val id: String,
    val items: List<CartItem>,
    val totals: CartTotals,
)

data class CartItem(
    val productId: String,
    val slug: String,
    val title: String,
    val artisanName: String,
    val imageUrl: String,
    val price: Double,
    val currency: String,
    val quantity: Int,
    val technique: String = "",
    val description: String = "",
)

data class CartTotals(
    val subtotal: Double,
    val estimatedShipping: Double,
    val tax: Double,
    val grandTotal: Double,
)