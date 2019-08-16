package com.veresz.pizza.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val city: String,
    val formattedAddress: String,
    val friendIds: List<String>,
    val id: String,
    val images: List<Image>,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val openingHours: List<String>,
    val phone: String,
    val website: String
) : Parcelable
