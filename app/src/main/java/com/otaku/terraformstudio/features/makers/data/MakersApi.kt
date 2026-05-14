package com.otaku.terraformstudio.features.makers.data

import com.otaku.terraformstudio.core.data.ImageDto
import com.otaku.terraformstudio.core.data.StudioStoryDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface MakersApi {
    @GET("artisans")
    suspend fun getArtisans(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): PaginatedArtisansDto
}

@Serializable
data class PaginatedArtisansDto(
    val data: List<ArtisanDirectoryDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

@Serializable
data class ArtisanDirectoryDto(
    @SerialName("_id") val id: String,
    val slug: String,
    val displayName: String,
    val bioShort: String? = null,
    val bioLong: String? = null,
    val heroImage: ImageDto? = null,
    val location: ArtisanLocationDto? = null,
    val studioStory: StudioStoryDto? = null
)

@Serializable
data class ArtisanLocationDto(
    val city: String? = null,
    val country: String? = null
)
