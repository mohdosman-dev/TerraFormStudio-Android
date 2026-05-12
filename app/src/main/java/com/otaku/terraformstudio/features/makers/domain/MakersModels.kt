package com.otaku.terraformstudio.features.makers.domain

import com.otaku.terraformstudio.core.utils.Constants

data class MakersList(
    val artisans: List<MakersArtisan>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

data class MakersArtisan(
    val id: String,
    val slug: String,
    val displayName: String,
    val bioShort: String?,
    val bioLong: String?,
    val heroImageUrl: String?,
    val heroImageAlt: String?,
    val city: String?,
    val country: String?,
    val techniques: List<String>,
    val materials: List<String>
) {
    val location: String?
        get() = when {
            city != null && country != null -> "$city, $country"
            city != null -> city
            country != null -> country
            else -> null
        }

    val technique: String?
        get() = techniques.firstOrNull()

    val material: String?
        get() = materials.firstOrNull()

    val realHeroImageUrl: String?
        get() = heroImageUrl?.let {
            if (it.startsWith("http")) it else Constants.BASE_URL + "/" + it
        }
}
