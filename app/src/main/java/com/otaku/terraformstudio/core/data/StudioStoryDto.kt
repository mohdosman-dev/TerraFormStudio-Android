package com.otaku.terraformstudio.core.data

import kotlinx.serialization.Serializable

@Serializable
data class StudioStoryDto(
    val philosophy: String? = null,
    val materials: List<String> = emptyList(),
    val techniques: List<String> = emptyList(),
    val inspirations: List<String> = emptyList()
)
