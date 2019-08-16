package com.veresz.pizza.ui

import com.veresz.pizza.di.scope.ActivityScope
import com.veresz.pizza.ui.detail.DetailFragmentModule
import com.veresz.pizza.ui.map.MapFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MapFragmentModule::class, DetailFragmentModule::class])
    abstract fun mainActivity(): MainActivity
}
