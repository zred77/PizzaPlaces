package com.veresz.pizza.di

import com.veresz.pizza.ui.main.MainActivityModule
import dagger.Module

@Module(includes = [MainActivityModule::class])
abstract class BindingModule
