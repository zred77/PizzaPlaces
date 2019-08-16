package com.veresz.pizza.repository

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.PizzaPlacesResponse
import com.veresz.pizza.model.Place
import javax.inject.Inject

class PizzaRepositoryImpl @Inject constructor(
    private val service: PizzaService
) : PizzaRepository {

    override suspend fun getPizzaPlaces(): PizzaPlacesResponse {
        return service.getPizzaPlaces()
    }

    override suspend fun getPizzaPlaceDetail(id: String): Place {
        return service.getPizzaPlaceDetail(id)
    }

    override suspend fun getFriends(): List<Friend> {
        return service.getFriends()
    }
}
