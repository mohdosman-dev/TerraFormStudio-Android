package com.otaku.terraformstudio.features.product.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface ProductRepository {
    suspend fun getProductDetail(slug: String): Result<ProductDetail, DataError.Network>
}
