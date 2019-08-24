package com.veresz.pizza.api

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.PizzaPlacesResponse
import com.veresz.pizza.model.Place

class PizzaServiceImpl(
    private val context: Context,
    private val moshi: Moshi
) : PizzaService {

    override suspend fun getPizzaPlaces(): PizzaPlacesResponse {
        val json = loadJson(context, "pizza.json")
        return moshi.parseJson(json)
    }

    override suspend fun getPizzaPlaceDetail(id: String): Place {
        val json = loadJson(context, "$id.json")
        return moshi.parseJson(json)
    }

    override suspend fun getFriends(): List<Friend> {
        val json = loadJson(context, "friends.json")
        return moshi.parseJson(json)
    }
}

inline fun <reified T> Moshi.parseJson(json: String): T {
    val jsonAdapter = jsonAdapter<T>()
    return jsonAdapter.fromJson(json)!!
}

inline fun <reified T> Moshi.jsonAdapter(): JsonAdapter<T> {
    return this.adapter(T::class.java)
}

inline fun <reified T> Moshi.jsonListAdapter(): JsonAdapter<List<T>> {
    return Types.newParameterizedType(List::class.java, T::class.java).let {
        this.adapter<List<T>>(it)
    }
}

fun loadJson(context: Context, fileName: String): String {
    return context.assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}
