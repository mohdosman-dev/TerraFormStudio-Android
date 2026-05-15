package com.otaku.terraformstudio.features.product.data

import com.otaku.terraformstudio.features.product.domain.ProductRepository
import com.otaku.terraformstudio.features.product.presentation.ProductDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val productModule = module {
    single { get<Retrofit>().create(ProductApi::class.java) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    viewModel { parameters ->
        ProductDetailViewModel(
            slug = parameters.get(),
            productRepository = get(),
            cartRepository = get()
        )
    }
}
