package com.veresz.pizza.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Friend(
    val avatarUrl: String,
    val id: Int,
    val name: String
) : Parcelable
