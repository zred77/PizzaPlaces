package com.veresz.pizza.di

import com.veresz.pizza.repository.friend.FriendRepository
import com.veresz.pizza.repository.friend.FriendRepositoryImpl
import com.veresz.pizza.repository.place.PlaceRepository
import com.veresz.pizza.repository.place.PlaceRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlaceRepository(repository: PlaceRepositoryImpl): PlaceRepository

    @Binds
    abstract fun bindFriendRepository(repository: FriendRepositoryImpl): FriendRepository
}
