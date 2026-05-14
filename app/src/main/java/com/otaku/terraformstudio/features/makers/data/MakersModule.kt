package com.otaku.terraformstudio.features.makers.data

import com.otaku.terraformstudio.features.makers.domain.MakersRepository
import com.otaku.terraformstudio.features.makers.presentation.MakersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val makersModule = module {
    single { get<Retrofit>().create(MakersApi::class.java) }
    single<MakersRepository> { MakersRepositoryImpl(get()) }
    viewModel { MakersViewModel(get()) }
}
