package com.veresz.pizza.ui.map

import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
internal abstract class MapFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [MapModule::class])
    abstract fun mapFragment(): MapFragment

    @Module(includes = [MapModule.BindModule::class])
    internal class MapModule {

        @Module
        abstract class BindModule {

            @Binds
            @Named("fragment")
            abstract fun bindViewModelProviderFactory(factory: MapViewModel.Factory): ViewModelProvider.Factory
        }
    }
}
