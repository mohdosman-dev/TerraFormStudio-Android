package com.otaku.terraformstudio.features.home.domain

data class HomeConfiguration(
    val sections: List<HomeSection>
)

data class HomeSection(
    val type: HomeSectionType,
    val title: String?,
    val subtitle: String?,
    val imageUrl: String?,
    val imageAlt: String?,
    val ctaLabel: String?,
    val ctaTarget: String?,
    val content: String?,
    val artisan: HomeArtisan?,
    val products: List<HomeProduct>,
    val collections: List<HomeCollection>,
    val sortOrder: Int
)

enum class HomeSectionType {
    HERO, ARTISAN_SPOTLIGHT, PRODUCT_ROW, COLLECTION_ROW, EDITORIAL, UNKNOWN
}

data class HomeArtisan(
    val id: String,
    val name: String,
    val philosophy: String?,
    val studioImageUrl: String?
)

data class HomeProduct(
    val id: String,
    val slug: String,
    val title: String,
    val price: Double,
    val imageUrl: String
)

data class HomeCollection(
    val id: String,
    val slug: String,
    val title: String,
    val imageUrl: String?
)
