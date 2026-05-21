package com.otaku.terraformstudio.features.checkout.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.checkout.domain.CheckoutRepository
import com.otaku.terraformstudio.features.checkout.domain.CheckoutSession
import com.otaku.terraformstudio.features.checkout.domain.DeliveryMethod
import com.otaku.terraformstudio.features.checkout.domain.DeliveryOption
import com.otaku.terraformstudio.features.checkout.domain.OrderConfirmation
import com.otaku.terraformstudio.features.checkout.domain.PriceSummary
import com.otaku.terraformstudio.features.checkout.domain.ShippingAddress
import retrofit2.HttpException
import java.io.IOException

class CheckoutRepositoryImpl(
    private val checkoutApi: CheckoutApi,
) : CheckoutRepository {

    override suspend fun getDeliveryMethods(): Result<List<DeliveryMethod>, DataError.Network> {
        return try {
            val dtos = checkoutApi.getDeliveryMethods()
            Result.Success(dtos.deliveryMethods.map { it.toDomain() })
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun initCheckout(cartId: String): Result<CheckoutSession, DataError.Network> {
        return try {
            val dto = checkoutApi.initCheckout(InitCheckoutRequestDto(cartId))
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> Result.Error(DataError.Network.NOT_FOUND)
                409 -> Result.Error(DataError.Network.CONFLICT)
                else -> Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun updateShipping(
        sessionId: String,
        address: ShippingAddress,
        deliveryMethodId: String,
    ): Result<CheckoutSession, DataError.Network> {
        return try {
            val dto = checkoutApi.updateShipping(
                sessionId = sessionId,
                body = UpdateShippingRequestDto(
                    shippingAddress = address.toDto(),
                    deliveryOption = deliveryMethodId,
                ),
            )
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            Result.Error(DataError.Network.SERVER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun completeCheckout(sessionId: String): Result<OrderConfirmation, DataError.Network> {
        return try {
            val dto = checkoutApi.completeCheckout(sessionId)
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.Error(DataError.Network.BAD_REQUEST)
                else -> Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}

private fun DeliveryMethodDto.toDomain() = DeliveryMethod(
    id = id,
    name = name,
    description = description,
    price = price,
    currency = currency,
    estimatedDays = estimatedDays,
    isActive = isActive,
    isDefault = isDefault,
)

private fun CheckoutSessionDto.toDomain() = CheckoutSession(
    id = id,
    status = status,
    shippingAddress = shippingAddress?.toDomain(),
    deliveryOption = deliveryOption?.toDomain(),
    priceSummary = priceValidation.toDomain(),
)

private fun ShippingAddressDto.toDomain() = ShippingAddress(
    fullName = fullName,
    phone = phone,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    city = city,
    state = state,
    postalCode = postalCode,
    country = country,
)

private fun DeliveryOptionDto.toDomain() = DeliveryOption(
    method = method,
    price = price,
    estimatedDays = estimatedDays,
)

private fun PriceValidationDto.toDomain() = PriceSummary(
    subtotal = subtotal,
    shipping = shipping,
    tax = tax,
    grandTotal = grandTotal,
)

private fun CompleteCheckoutResponseDto.toDomain() = OrderConfirmation(
    orderId = orderId,
    status = status,
    grandTotal = grandTotal,
)

private fun ShippingAddress.toDto() = ShippingAddressDto(
    fullName = fullName,
    phone = phone,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    city = city,
    state = state,
    postalCode = postalCode,
    country = country,
)
