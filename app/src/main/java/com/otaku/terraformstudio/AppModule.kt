package com.otaku.terraformstudio

import com.otaku.terraformstudio.core.data.CoreDataModule
import com.otaku.terraformstudio.features.home.data.HomeModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoreDataModule::class,
        HomeModule::class
    ]
)
class AppModule
