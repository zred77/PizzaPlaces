package com.veresz.pizza.di

import com.veresz.pizza.repository.PizzaRepository
import com.veresz.pizza.repository.PizzaRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(repository: PizzaRepositoryImpl): PizzaRepository
}
