package com.otaku.terraformstudio.features.artisan.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface ArtisanRepository {
    suspend fun getArtisanProfile(slug: String): Result<ArtisanProfile, DataError.Network>
}
