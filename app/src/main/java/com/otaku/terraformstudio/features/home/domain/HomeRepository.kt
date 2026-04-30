package com.otaku.terraformstudio.features.home.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface HomeRepository {
    suspend fun getHomeConfiguration(): Result<HomeConfiguration, DataError.Network>
}
