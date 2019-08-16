package com.veresz.pizza.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val caption: String,
    val expiration: String,
    val id: String,
    val url: String
) : Parcelable
