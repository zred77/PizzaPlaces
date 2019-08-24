package com.veresz.pizza.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
@Entity
data class Friend(
    @PrimaryKey val id: Int,
    val avatarUrl: String,
    val name: String
) : Parcelable
