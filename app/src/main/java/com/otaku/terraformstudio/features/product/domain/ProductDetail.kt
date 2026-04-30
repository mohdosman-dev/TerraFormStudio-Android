package com.otaku.terraformstudio.features.product.domain

import com.otaku.terraformstudio.features.home.domain.HomeArtisan
import com.otaku.terraformstudio.features.home.domain.HomeProduct

data class ProductDetail(
    val id: String,
    val slug: String,
    val title: String,
    val subtitle: String?,
    val descriptionLong: String,
    val price: Double,
    val currency: String,
    val media: List<ProductMedia>,
    val specifications: ProductSpecifications,
    val artisan: HomeArtisan,
    val relatedProducts: List<HomeProduct>
)

data class ProductMedia(
    val url: String,
    val alt: String,
    val type: String
)

data class ProductSpecifications(
    val material: String,
    val technique: String,
    val glaze: String,
    val care: String,
    val dimensions: String?
)
