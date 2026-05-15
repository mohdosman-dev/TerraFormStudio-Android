package com.otaku.terraformstudio.features.cart.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CartApi {
    @GET("cart")
    suspend fun getCart(): CartDto

    @POST("cart/items")
    suspend fun addItem(@Body body: AddToCartRequestDto): CartDto

    @PATCH("cart/items/{productId}")
    suspend fun updateItemQuantity(
        @Path("productId") productId: String,
        @Body body: UpdateCartItemRequestDto,
    ): CartDto

    @DELETE("cart/items/{productId}")
    suspend fun removeItem(@Path("productId") productId: String): CartDto

    @POST("cart/merge")
    suspend fun mergeCart(@Body body: MergeCartRequestDto): CartDto
}