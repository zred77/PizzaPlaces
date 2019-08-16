package com.veresz.pizza.di

import com.veresz.pizza.BuildConfig
import com.veresz.pizza.api.PizzaService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun pizzaService(client: OkHttpClient): PizzaService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PIZZA_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PizzaService::class.java)
    }
}
