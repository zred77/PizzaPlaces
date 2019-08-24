package com.veresz.pizza.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.veresz.pizza.model.Friend

@Dao
interface FriendDao {

    @Query("SELECT * FROM friend")
    suspend fun getAll(): List<Friend>

    @Insert
    suspend fun insert(vararg friend: Friend)

    @Query("DELETE FROM friend")
    suspend fun deleteAll()
}
