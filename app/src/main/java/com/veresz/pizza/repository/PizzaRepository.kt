package com.veresz.pizza.repository

import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.PizzaPlacesResponse
import com.veresz.pizza.model.Place

interface PizzaRepository {

    suspend fun getPizzaPlaces(): PizzaPlacesResponse

    suspend fun getPizzaPlaceDetail(id: String): Place

    suspend fun getFriends(): List<Friend>
}
