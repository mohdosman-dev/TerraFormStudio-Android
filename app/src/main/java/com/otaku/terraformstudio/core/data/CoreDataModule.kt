package com.otaku.terraformstudio.core.data

import com.otaku.terraformstudio.core.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val coreDataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single<Retrofit> {
        val contentType = "application/json".toMediaType()
        val baseUrl = if (Constants.BASE_URL.endsWith("/")) {
            "${Constants.BASE_URL}api/"
        } else {
            "${Constants.BASE_URL}/api/"
        }
        
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .build()
    }
}
