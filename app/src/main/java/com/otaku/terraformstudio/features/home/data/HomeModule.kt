package com.otaku.terraformstudio.features.home.data

import com.otaku.terraformstudio.features.home.domain.HomeRepository
import com.otaku.terraformstudio.features.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val homeModule = module {
    single { get<Retrofit>().create(HomeApi::class.java) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    viewModel { HomeViewModel(get()) }
}
