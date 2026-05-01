package com.otaku.terraformstudio.features.product.data

import com.otaku.terraformstudio.features.home.data.toDomain
import com.otaku.terraformstudio.features.product.domain.ProductDetail
import com.otaku.terraformstudio.features.product.domain.ProductMedia
import com.otaku.terraformstudio.features.product.domain.ProductSpecifications

fun FullProductDto.toDomain(): ProductDetail {
    return ProductDetail(
        id = id,
        slug = slug,
        title = title,
        subtitle = subtitle,
        descriptionLong = descriptionLong,
        price = price.amount,
        currency = price.currency,
        media = media.map { it.toDomain() },
        specifications = specifications.toDomain(),
        artisan = artisanId.toDomain(),
        relatedProducts = discovery?.relatedProductIds?.map { it.toDomain() } ?: emptyList()
    )
}

fun ProductMediaDto.toDomain(): ProductMedia {
    return ProductMedia(
        url = url,
        alt = alt,
        type = type
    )
}

fun SpecificationsDto.toDomain(): ProductSpecifications {
    val dimStr = dimensions?.let {
        listOfNotNull(
            it.widthCm?.let { w -> "${w.toInt()}\"" },
            it.heightCm?.let { h -> "${h.toInt()}\"" }
        ).joinToString(" x ")
    }

    return ProductSpecifications(
        material = material,
        technique = technique,
        glaze = glaze,
        care = care,
        dimensions = dimStr
    )
}

// Extension to allow sorting of media
private val ProductMediaDto.sortOrderValue: Int get() = sortOrder
