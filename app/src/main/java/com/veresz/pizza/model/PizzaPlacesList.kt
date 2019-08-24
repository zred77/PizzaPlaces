package com.veresz.pizza.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PizzaPlacesList(
    val places: List<Place>
) : Parcelable
