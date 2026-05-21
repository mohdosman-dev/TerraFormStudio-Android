package com.otaku.terraformstudio.features.checkout.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutApi {
    @GET("checkout/delivery-methods")
    suspend fun getDeliveryMethods(): DeliveryMethodResponseDto

    @POST("checkout/start")
    suspend fun initCheckout(@Body body: InitCheckoutRequestDto): CheckoutSessionDto

    @PATCH("checkout/{sessionId}/shipping")
    suspend fun updateShipping(
        @Path("sessionId") sessionId: String,
        @Body body: UpdateShippingRequestDto,
    ): CheckoutSessionDto

    @POST("checkout/{sessionId}/complete")
    suspend fun completeCheckout(@Path("sessionId") sessionId: String): CompleteCheckoutResponseDto
}
