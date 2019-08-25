package com.veresz.pizza.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.veresz.pizza.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(app: App): Context = app.applicationContext

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun providesMoshi(): Moshi {
            return Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }
    }
}
