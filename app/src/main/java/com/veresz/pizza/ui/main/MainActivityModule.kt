package com.veresz.pizza.ui.main

import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.di.scope.ActivityScope
import com.veresz.pizza.ui.detail.DetailFragmentModule
import com.veresz.pizza.ui.map.MapFragmentModule
import com.veresz.pizza.ui.maplist.MapListFragmentModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MapFragmentModule::class,
            MapListFragmentModule::class,
            DetailFragmentModule::class,
            MainModule::class]
    )
    abstract fun mainActivity(): MainActivity

    @Module(includes = [MainModule.BindModule::class])
    internal class MainModule {

        @Module
        abstract class BindModule {

            @Binds
            @Named("activity")
            abstract fun bindViewModelProviderFactory(factory: MainViewModel.Factory): ViewModelProvider.Factory
        }
    }
}
