package com.otaku.terraformstudio

import com.otaku.terraformstudio.core.data.coreDataModule
import com.otaku.terraformstudio.features.artisan.data.artisanModule
import com.otaku.terraformstudio.features.home.data.homeModule
import com.otaku.terraformstudio.features.makers.data.makersModule
import com.otaku.terraformstudio.features.product.data.productModule
import org.koin.dsl.module

val appModule = module {
    includes(coreDataModule, homeModule, productModule, artisanModule, makersModule)
}
