package com.otaku.terraformstudio.core.data

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val url: String,
    val alt: String
)
