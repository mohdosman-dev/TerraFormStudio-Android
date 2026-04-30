package com.otaku.terraformstudio.features.home.data

import com.otaku.terraformstudio.features.home.domain.HomeArtisan
import com.otaku.terraformstudio.features.home.domain.HomeCollection
import com.otaku.terraformstudio.features.home.domain.HomeConfiguration
import com.otaku.terraformstudio.features.home.domain.HomeProduct
import com.otaku.terraformstudio.features.home.domain.HomeSection
import com.otaku.terraformstudio.features.home.domain.HomeSectionType

fun HomeConfigurationDto.toDomain(): HomeConfiguration {
    return HomeConfiguration(
        sections = sections.map { it.toDomain() }.sortedBy { it.sortOrder }
    )
}

fun HomeSectionDto.toDomain(): HomeSection {
    return HomeSection(
        type = when (type) {
            "hero" -> HomeSectionType.HERO
            "artisan_spotlight" -> HomeSectionType.ARTISAN_SPOTLIGHT
            "product_row" -> HomeSectionType.PRODUCT_ROW
            "collection_row" -> HomeSectionType.COLLECTION_ROW
            "editorial" -> HomeSectionType.EDITORIAL
            else -> HomeSectionType.UNKNOWN
        },
        title = title,
        subtitle = subtitle,
        imageUrl = image?.url,
        imageAlt = image?.alt,
        ctaLabel = cta?.label,
        ctaTarget = cta?.targetId ?: cta?.url,
        content = content,
        artisan = artisanId?.toDomain(),
        products = productIds?.map { it.toDomain() } ?: emptyList(),
        collections = collectionIds?.map { it.toDomain() } ?: emptyList(),
        sortOrder = sortOrder
    )
}

fun ArtisanDto.toDomain(): HomeArtisan {
    return HomeArtisan(
        id = id,
        name = displayName,
        philosophy = studioStory?.philosophy,
        studioImageUrl = heroImage?.url
    )
}

fun ProductDto.toDomain(): HomeProduct {
    return HomeProduct(
        id = id,
        title = title,
        price = price.amount,
        imageUrl = media?.firstOrNull()?.url ?: ""
    )
}

fun CollectionDto.toDomain(): HomeCollection {
    return HomeCollection(
        id = id,
        title = title,
        imageUrl = heroImage?.url
    )
}
