package com.veresz.pizza.di

import com.squareup.moshi.Moshi
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
    fun pizzaService(moshi: Moshi, client: OkHttpClient): PizzaService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PIZZA_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PizzaService::class.java)
    }

//    @Provides
//    fun provideMockPizzaService(context: Context, moshi: Moshi): PizzaService {
//        return PizzaServiceImpl(context, moshi)
//    }
}
