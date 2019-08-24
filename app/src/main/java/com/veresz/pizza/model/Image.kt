package com.veresz.pizza.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Image(
    val caption: String,
    val expiration: String,
    val id: String,
    val url: String
) : Parcelable
