package com.otaku.terraformstudio.core.data

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

@Module
@ComponentScan("com.otaku.terraformstudio.core.data")
class CoreDataModule {

    @Single
    fun provideRetrofit(): Retrofit {
        // 10.0.2.2 is the special alias to your host loopback interface (localhost on your development machine)
        return RetrofitFactory.create("http://10.0.2.2:3000/")
    }
}
