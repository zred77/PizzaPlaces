package com.veresz.pizza.repository.place

import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.Place

interface PlaceRepository {

    suspend fun getPizzaPlaces(refresh: Boolean = false): List<Place>

    suspend fun getPizzaPlaceDetail(id: String): Place

    suspend fun getFriends(): List<Friend>
}
