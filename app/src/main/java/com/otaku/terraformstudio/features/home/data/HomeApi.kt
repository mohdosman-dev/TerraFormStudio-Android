package com.otaku.terraformstudio.features.home.data

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface HomeApi {
    @GET("home")
    suspend fun getHomeConfiguration(): HomeConfigurationDto
}

@Serializable
data class HomeConfigurationDto(
    val name: String,
    val status: String,
    val sections: List<HomeSectionDto>
)

@Serializable
data class HomeSectionDto(
    val type: String,
    val title: String? = null,
    val subtitle: String? = null,
    val image: ImageDto? = null,
    val cta: CtaDto? = null,
    val content: String? = null,
    val artisanId: ArtisanDto? = null,
    val productIds: List<ProductDto>? = null,
    val collectionIds: List<CollectionDto>? = null,
    val sortOrder: Int
)

@Serializable
data class ImageDto(
    val url: String,
    val alt: String
)

@Serializable
data class CtaDto(
    val label: String,
    val targetType: String,
    val targetId: String? = null,
    val url: String? = null
)

@Serializable
data class ArtisanDto(
    val _id: String,
    val name: String,
    val philosophy: String? = null,
    val studioImage: String? = null
)

@Serializable
data class ProductDto(
    val _id: String,
    val title: String,
    val price: Double,
    val primaryImage: String,
    val artisan: String? = null // Usually just the ID or populated name
)

@Serializable
data class CollectionDto(
    val _id: String,
    val title: String,
    val description: String? = null,
    val image: String? = null
)
