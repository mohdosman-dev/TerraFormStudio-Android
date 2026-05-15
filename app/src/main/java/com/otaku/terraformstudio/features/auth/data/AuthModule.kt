package com.otaku.terraformstudio.features.auth.data

import com.otaku.terraformstudio.core.data.local.AuthTokenManager
import com.otaku.terraformstudio.features.auth.domain.AuthRepository
import com.otaku.terraformstudio.features.auth.presentation.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    viewModel { SignInViewModel(get(), get(), get()) }
}
