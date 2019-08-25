package com.veresz.pizza.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Entity
@Parcelize
data class Place(
    @PrimaryKey val id: String,
    val city: String,
    val formattedAddress: String,
    val friendIds: List<String>,
    val images: List<Image>,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val openingHours: List<String>,
    var phone: String?,
    val website: String
) : Parcelable, ClusterItem {

    @[IgnoredOnParcel Ignore] val rating: Float = (0..50).random().toFloat().div(10f)
    @[IgnoredOnParcel Ignore] val distanceFrom: Float = (5..100).random().toFloat().div(10f)

    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getPosition(): LatLng = LatLng(latitude, longitude)
}
