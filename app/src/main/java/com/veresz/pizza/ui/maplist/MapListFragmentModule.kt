package com.veresz.pizza.ui.maplist

import com.veresz.pizza.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MapListFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun mapFragment(): MapListFragment
}
