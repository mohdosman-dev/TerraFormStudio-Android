package com.otaku.terraformstudio.features.home.data

import retrofit2.http.GET

interface HomeApi {
    @GET("home")
    suspend fun getHomeConfiguration(): HomeConfigurationDto
}
