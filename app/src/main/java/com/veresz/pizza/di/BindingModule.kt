package com.veresz.pizza.di

import com.veresz.pizza.ui.MainActivityModule
import dagger.Module

@Module(includes = [MainActivityModule::class])
abstract class BindingModule
