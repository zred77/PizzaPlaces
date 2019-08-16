package com.veresz.pizza.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
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
) : Parcelable, ClusterItem {

    override fun getSnippet(): String {
        return ""
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getPosition(): LatLng = LatLng(latitude, longitude)
}
