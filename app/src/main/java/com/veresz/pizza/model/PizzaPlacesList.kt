package com.veresz.pizza.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PizzaPlacesList(
    val places: List<Place>
) : Parcelable
