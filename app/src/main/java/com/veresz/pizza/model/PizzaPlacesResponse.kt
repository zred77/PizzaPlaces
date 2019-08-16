package com.veresz.pizza.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PizzaPlacesResponse(
    val list: PizzaPlacesList
) : Parcelable
