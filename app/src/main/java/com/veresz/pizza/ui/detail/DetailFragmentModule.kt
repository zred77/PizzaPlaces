package com.veresz.pizza.ui.detail

import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.di.scope.FragmentScope
import com.veresz.pizza.model.Place
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
internal abstract class DetailFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ProviderModule::class])
    abstract fun detailFragment(): DetailFragment

    @Module(includes = [ProviderModule.BindModule::class])
    internal class ProviderModule {

        @Provides
        fun providePizzaPlace(detailFragment: DetailFragment): Place {
            return detailFragment.getPizzaPlace()
        }

        @Module
        internal abstract class BindModule {

            @Binds
            @Named("fragment")
            abstract fun bindViewModelProviderFactory(factory: DetailViewModel.Factory): ViewModelProvider.Factory
        }
    }
}
