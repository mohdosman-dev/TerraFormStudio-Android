package com.otaku.terraformstudio.features.home.data

import com.otaku.terraformstudio.core.data.ImageDto
import com.otaku.terraformstudio.core.data.StudioStoryDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
data class CtaDto(
    val label: String,
    val targetType: String? = null,
    val targetId: String? = null,
    val url: String? = null
)

@Serializable
data class ArtisanDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val displayName: String,
    val studioStory: StudioStoryDto? = null,
    val heroImage: ImageDto? = null
)


@Serializable
data class ProductDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val title: String,
    val price: PriceDto,
    val media: List<ImageDto>? = null,
    val descriptionLong: String? = null,
    val specifications: ProductSpecsDto? = null,
)

@Serializable
data class ProductSpecsDto(
    val technique: String? = null,
)

@Serializable
data class PriceDto(
    val amount: Double,
    val currency: String
)

@Serializable
data class CollectionDto(
    @SerialName("_id")
    val id: String,
    val slug: String,
    val title: String,
    val description: String? = null,
    val heroImage: ImageDto? = null
)
