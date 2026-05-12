package com.otaku.terraformstudio.features.makers.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface MakersRepository {
    suspend fun getArtisans(page: Int = 1, limit: Int = 10): Result<MakersList, DataError.Network>
}
