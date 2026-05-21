package com.otaku.terraformstudio.features.checkout.data

import com.otaku.terraformstudio.features.checkout.domain.CheckoutRepository
import com.otaku.terraformstudio.features.checkout.presentation.CheckoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val checkoutModule = module {
    single { get<Retrofit>().create(CheckoutApi::class.java) }
    single<CheckoutRepository> { CheckoutRepositoryImpl(get()) }
    viewModel { CheckoutViewModel(get()) }
}
