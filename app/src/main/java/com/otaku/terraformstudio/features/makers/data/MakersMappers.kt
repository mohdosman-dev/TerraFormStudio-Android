package com.otaku.terraformstudio.features.makers.data

import com.otaku.terraformstudio.features.makers.domain.MakersArtisan
import com.otaku.terraformstudio.features.makers.domain.MakersList

fun PaginatedArtisansDto.toDomain(): MakersList {
    return MakersList(
        artisans = data.map { it.toDomain() },
        total = total,
        page = page,
        limit = limit,
        totalPages = totalPages
    )
}

fun ArtisanDirectoryDto.toDomain(): MakersArtisan {
    return MakersArtisan(
        id = id,
        slug = slug,
        displayName = displayName,
        bioShort = bioShort,
        bioLong = bioLong,
        heroImageUrl = heroImage?.url,
        heroImageAlt = heroImage?.alt,
        city = location?.city,
        country = location?.country,
        techniques = studioStory?.techniques ?: emptyList(),
        materials = studioStory?.materials ?: emptyList()
    )
}
