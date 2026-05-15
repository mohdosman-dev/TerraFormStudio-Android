package com.otaku.terraformstudio.features.cart.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface CartRepository {
    suspend fun getCart(): Result<Cart, DataError.Network>
    suspend fun addItem(productId: String, quantity: Int): Result<Cart, DataError.Network>
    suspend fun updateItemQuantity(productId: String, quantity: Int): Result<Cart, DataError.Network>
    suspend fun removeItem(productId: String): Result<Cart, DataError.Network>
    suspend fun mergeGuestCart(guestId: String): Result<Cart, DataError.Network>
}