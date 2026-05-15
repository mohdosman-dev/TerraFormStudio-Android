package com.otaku.terraformstudio.features.cart.data

import com.otaku.terraformstudio.features.cart.domain.CartRepository
import com.otaku.terraformstudio.features.cart.presentation.CartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val cartModule = module {
    single { get<Retrofit>().create(CartApi::class.java) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    viewModel { CartViewModel(get()) }
}