package com.otaku.terraformstudio.features.artisan.domain

import com.otaku.terraformstudio.core.utils.Constants

data class ArtisanProfile(
    val id: String,
    val slug: String,
    val displayName: String,
    val brandName: String,
    val bioShort: String?,
    val bioLong: String?,
    val studioStory: ArtisanStudioStory?,
    val heroImage: ArtisanImage?,
    val gallery: List<ArtisanImage>,
    val location: ArtisanLocation?,
    val socialLinks: ArtisanSocialLinks?,
    val products: List<ArtisanProduct>
)

data class ArtisanStudioStory(
    val philosophy: String?,
    val materials: List<String>,
    val techniques: List<String>
)

data class ArtisanImage(
    val url: String,
    val alt: String
) {
    val realUrl: String = if (url.startsWith("http")) url else Constants.BASE_URL + "/" + url
}

data class ArtisanLocation(
    val city: String?,
    val country: String?
)

data class ArtisanSocialLinks(
    val instagram: String?
)

data class ArtisanProduct(
    val id: String,
    val slug: String,
    val title: String,
    val subtitle: String?,
    val price: Double,
    val currency: String,
    val imageUrl: String?,
    val material: String?,
    val collection: String?
) {
    val realImageUrl: String? = imageUrl?.let {
        if (it.startsWith("http")) it else Constants.BASE_URL + "/" + it
    }
}
