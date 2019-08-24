package com.veresz.pizza.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.veresz.pizza.model.Place

@Dao
interface PlaceDao {

    @Query("SELECT * FROM place")
    suspend fun getAll(): List<Place>

    @Insert
    suspend fun insert(vararg place: Place)

    @Query("DELETE FROM place")
    suspend fun deleteAll()
}
