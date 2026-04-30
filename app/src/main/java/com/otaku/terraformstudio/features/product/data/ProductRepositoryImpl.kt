package com.otaku.terraformstudio.features.product.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.product.domain.ProductDetail
import com.otaku.terraformstudio.features.product.domain.ProductRepository
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl(
    private val productApi: ProductApi
) : ProductRepository {
    override suspend fun getProductDetail(slug: String): Result<ProductDetail, DataError.Network> {
        return try {
            val dto = productApi.getProductDetail(slug)
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                404 -> Result.Error(DataError.Network.NOT_FOUND)
                else -> Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
