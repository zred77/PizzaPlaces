package com.veresz.pizza.repository.place

import com.veresz.pizza.api.PizzaService
import com.veresz.pizza.db.dao.PlaceDao
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.Place
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val service: PizzaService,
    private val placeDao: PlaceDao
) : PlaceRepository {

    override suspend fun getPizzaPlaces(refresh: Boolean): List<Place> {
        var places = placeDao.getAll()
        if (refresh || places.isEmpty()) {
            places = service.getPizzaPlaces().list.places
            placeDao.deleteAll()
            placeDao.insert(*places.toTypedArray())
        }
        return places
    }

    override suspend fun getPizzaPlaceDetail(id: String): Place {
        return service.getPizzaPlaceDetail(id)
    }

    override suspend fun getFriends(): List<Friend> {
        return service.getFriends()
    }
}
