package com.otaku.terraformstudio.features.checkout.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface CheckoutRepository {
    suspend fun getDeliveryMethods(): Result<List<DeliveryMethod>, DataError.Network>
    suspend fun initCheckout(cartId: String): Result<CheckoutSession, DataError.Network>
    suspend fun updateShipping(
        sessionId: String,
        address: ShippingAddress,
        deliveryMethodId: String,
    ): Result<CheckoutSession, DataError.Network>
    suspend fun completeCheckout(sessionId: String): Result<OrderConfirmation, DataError.Network>
}
