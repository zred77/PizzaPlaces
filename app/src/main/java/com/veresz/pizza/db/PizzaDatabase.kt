package com.veresz.pizza.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.veresz.pizza.db.converter.ImageListConverter
import com.veresz.pizza.db.converter.StringListConverter
import com.veresz.pizza.db.dao.FriendDao
import com.veresz.pizza.db.dao.PlaceDao
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.Place

@Database(
    entities = [
        Friend::class,
        Place::class],
    version = 1
)
@TypeConverters(
    value = [
        StringListConverter::class,
        ImageListConverter::class
    ]
)
abstract class PizzaDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao

    abstract fun placeDao(): PlaceDao
}
