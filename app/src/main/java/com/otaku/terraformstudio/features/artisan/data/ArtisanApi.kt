package com.otaku.terraformstudio.features.artisan.data

import com.otaku.terraformstudio.core.data.ImageDto
import com.otaku.terraformstudio.core.data.StudioStoryDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtisanApi {
    @GET("artisans/{slug}")
    suspend fun getArtisanProfile(@Path("slug") slug: String): ArtisanProfileDto
}

@Serializable
data class ArtisanProfileDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val displayName: String,
    val brandName: String,
    val bioShort: String? = null,
    val bioLong: String? = null,
    val studioStory: StudioStoryDto? = null,
    val heroImage: ImageDto? = null,
    val gallery: List<ImageDto> = emptyList(),
    val location: LocationDto? = null,
    val socialLinks: SocialLinksDto? = null,
    val products: List<ArtisanProductDto> = emptyList()
)

@Serializable
data class LocationDto(
    val city: String? = null,
    val country: String? = null
)

@Serializable
data class SocialLinksDto(
    val instagram: String? = null
)

@Serializable
data class ArtisanProductDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val title: String,
    val subtitle: String? = null,
    val price: ArtisanProductPriceDto,
    val media: List<ArtisanProductMediaDto>? = null,
    val specifications: ArtisanProductSpecsDto? = null
)

@Serializable
data class ArtisanProductPriceDto(
    val amount: Double,
    val currency: String
)

@Serializable
data class ArtisanProductMediaDto(
    val url: String,
    val alt: String,
    val type: String = "image",
    val sortOrder: Int = 0
)

@Serializable
data class ArtisanProductSpecsDto(
    val material: String? = null
)
