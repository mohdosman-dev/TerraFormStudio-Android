package com.otaku.terraformstudio.features.product.data

import com.otaku.terraformstudio.features.home.data.ArtisanDto
import com.otaku.terraformstudio.features.home.data.PriceDto
import com.otaku.terraformstudio.features.home.data.ProductDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products/{slug}")
    suspend fun getProductDetail(@Path("slug") slug: String): FullProductDto
}

@Serializable
data class FullProductDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val title: String,
    val subtitle: String? = null,
    val descriptionLong: String,
    val price: PriceDto,
    val media: List<ProductMediaDto>,
    val specifications: SpecificationsDto,
    val artisanId: ArtisanDto,
    val discovery: DiscoveryDto? = null
)

@Serializable
data class ProductMediaDto(
    val url: String,
    val alt: String,
    val type: String,
    val sortOrder: Int
)

@Serializable
data class SpecificationsDto(
    val material: String,
    val technique: String,
    val glaze: String,
    val care: String,
    val dimensions: DimensionsDto? = null
)

@Serializable
data class DimensionsDto(
    val widthCm: Double? = null,
    val heightCm: Double? = null,
    val weightGrams: Double? = null
)

@Serializable
data class DiscoveryDto(
    val relatedProductIds: List<ProductDto> = emptyList()
)
