package com.otaku.terraformstudio.features.artisan.data

import com.otaku.terraformstudio.core.data.ImageDto
import com.otaku.terraformstudio.core.data.StudioStoryDto
import com.otaku.terraformstudio.features.artisan.domain.ArtisanImage
import com.otaku.terraformstudio.features.artisan.domain.ArtisanLocation
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProduct
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProfile
import com.otaku.terraformstudio.features.artisan.domain.ArtisanSocialLinks
import com.otaku.terraformstudio.features.artisan.domain.ArtisanStudioStory

fun ArtisanProfileDto.toDomain(): ArtisanProfile {
    return ArtisanProfile(
        id = id,
        slug = slug,
        displayName = displayName,
        brandName = brandName,
        bioShort = bioShort,
        bioLong = bioLong,
        studioStory = studioStory?.toDomain(),
        heroImage = heroImage?.toDomain(),
        gallery = gallery.map { it.toDomain() },
        location = location?.toDomain(),
        socialLinks = socialLinks?.toDomain(),
        products = products.map { it.toDomain() }
    )
}

fun StudioStoryDto.toDomain(): ArtisanStudioStory {
    return ArtisanStudioStory(
        philosophy = philosophy,
        materials = materials,
        techniques = techniques
    )
}

fun ImageDto.toDomain(): ArtisanImage {
    return ArtisanImage(
        url = url,
        alt = alt
    )
}

fun LocationDto.toDomain(): ArtisanLocation {
    return ArtisanLocation(
        city = city,
        country = country
    )
}

fun SocialLinksDto.toDomain(): ArtisanSocialLinks {
    return ArtisanSocialLinks(
        instagram = instagram
    )
}

fun ArtisanProductDto.toDomain(): ArtisanProduct {
    return ArtisanProduct(
        id = id,
        slug = slug,
        title = title,
        subtitle = subtitle,
        price = price.amount,
        currency = price.currency,
        imageUrl = media?.firstOrNull { it.sortOrder == 0 }?.url
            ?: media?.firstOrNull()?.url,
        material = specifications?.material,
        collection = subtitle
    )
}
