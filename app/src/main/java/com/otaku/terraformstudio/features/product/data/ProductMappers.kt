package com.otaku.terraformstudio.features.product.data

import com.otaku.terraformstudio.features.home.data.toDomain
import com.otaku.terraformstudio.features.product.domain.ProductDetail
import com.otaku.terraformstudio.features.product.domain.ProductMedia
import com.otaku.terraformstudio.features.product.domain.ProductSpecifications

fun FullProductDto.toDomain(): ProductDetail {
    return ProductDetail(
        id = _id,
        slug = slug,
        title = title,
        subtitle = subtitle,
        descriptionLong = descriptionLong,
        price = price.amount,
        currency = price.currency,
        media = media.map { it.toDomain() }.sortedBy { it.sortOrder },
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
            it.widthCm?.let { w -> "${w}cm w" },
            it.heightCm?.let { h -> "${h}cm h" },
            it.weightGrams?.let { g -> "${g}g" }
        ).joinToString(", ")
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
