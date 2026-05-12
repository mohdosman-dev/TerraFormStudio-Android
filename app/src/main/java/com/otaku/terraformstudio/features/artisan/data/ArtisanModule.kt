package com.otaku.terraformstudio.features.artisan.data

import com.otaku.terraformstudio.features.artisan.domain.ArtisanRepository
import com.otaku.terraformstudio.features.artisan.presentation.ArtisanProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val artisanModule = module {
    single { get<Retrofit>().create(ArtisanApi::class.java) }
    single<ArtisanRepository> { ArtisanRepositoryImpl(get()) }
    viewModel { parameters -> ArtisanProfileViewModel(slug = parameters.get(), get()) }
}
