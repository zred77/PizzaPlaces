package com.veresz.pizza.di

import android.content.Context
import androidx.room.Room
import com.veresz.pizza.db.PizzaDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun providesDatabase(context: Context): PizzaDatabase {
        return Room.databaseBuilder(
            context,
            PizzaDatabase::class.java,
            "pizza-db"
        ).build()
    }

    @Provides
    fun providesFriendDao(db: PizzaDatabase) = db.friendDao()

    @Provides
    fun providesPlaceDao(db: PizzaDatabase) = db.placeDao()
}
