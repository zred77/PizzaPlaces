package com.veresz.pizza.di

import android.content.Context
import com.veresz.pizza.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(app: App): Context = app.applicationContext
}
