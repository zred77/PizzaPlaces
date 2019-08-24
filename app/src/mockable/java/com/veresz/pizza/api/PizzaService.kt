package com.veresz.pizza.api

import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.PizzaPlacesResponse
import com.veresz.pizza.model.Place
import retrofit2.http.GET
import retrofit2.http.Path

interface PizzaService {

    @GET("pizzaplaces")
    suspend fun getPizzaPlaces(): PizzaPlacesResponse

    @GET("pizzaplaces/{id}")
    suspend fun getPizzaPlaceDetail(@Path("id") id: String): Place

    @GET("friends")
    suspend fun getFriends(): List<Friend>
}
