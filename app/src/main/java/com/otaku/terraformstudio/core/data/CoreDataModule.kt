package com.otaku.terraformstudio.core.data

import com.otaku.terraformstudio.core.data.local.AuthTokenManager
import com.otaku.terraformstudio.core.data.local.GuestTokenManager
import com.otaku.terraformstudio.core.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val coreDataModule = module {
    single { AuthTokenManager(get()) }

    single { GuestTokenManager(get()) }

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

        val authTokenManager: AuthTokenManager = get()
        val guestTokenManager: GuestTokenManager = get()

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val token = authTokenManager.getToken()
                val request = if (token != null) {
                    original.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    original.newBuilder()
                        .header("X-Guest-Token", guestTokenManager.getGuestId())
                        .build()
                }
                chain.proceed(request)
            }
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
