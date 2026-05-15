package com.otaku.terraformstudio.features.cart.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.cart.domain.Cart
import com.otaku.terraformstudio.features.cart.domain.CartItem
import com.otaku.terraformstudio.features.cart.domain.CartRepository
import com.otaku.terraformstudio.features.cart.domain.CartTotals
import retrofit2.HttpException
import java.io.IOException

class CartRepositoryImpl(
    private val cartApi: CartApi,
) : CartRepository {
    override suspend fun getCart(): Result<Cart, DataError.Network> {
        return try {
            val dto = cartApi.getCart()
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Result.Error(DataError.Network.NOT_FOUND)
                else -> Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun addItem(productId: String, quantity: Int): Result<Cart, DataError.Network> {
        return try {
            val dto = cartApi.addItem(AddToCartRequestDto(productId = productId, quantity = quantity))
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateItemQuantity(productId: String, quantity: Int): Result<Cart, DataError.Network> {
        return try {
            val dto = cartApi.updateItemQuantity(productId, UpdateCartItemRequestDto(quantity = quantity))
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun removeItem(productId: String): Result<Cart, DataError.Network> {
        return try {
            val dto = cartApi.removeItem(productId)
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun mergeGuestCart(guestId: String): Result<Cart, DataError.Network> {
        return try {
            val dto = cartApi.mergeCart(MergeCartRequestDto(guestId = guestId))
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

private fun CartDto.toDomain() = Cart(
    id = id,
    items = items.map { it.toDomain() },
    totals = totals.toDomain(),
)

private fun CartItemDto.toDomain() = CartItem(
    productId = product.id,
    slug = product.slug,
    title = product.title,
    artisanName = "",
    imageUrl = product.media?.firstOrNull()?.url ?: "",
    price = product.price.amount,
    currency = product.price.currency,
    technique = product.specifications?.technique ?: "",
    description = product.descriptionLong ?: "",
    quantity = quantity,
)

private fun CartTotalsDto.toDomain() = CartTotals(
    subtotal = subtotal,
    estimatedShipping = estimatedShipping,
    tax = tax,
    grandTotal = grandTotal,
)