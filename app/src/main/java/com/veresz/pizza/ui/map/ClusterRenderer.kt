package com.veresz.pizza.ui.map

import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.veresz.pizza.R
import com.veresz.pizza.model.Place
import com.veresz.pizza.ui.main.MainViewModel

class ClusterRenderer(
    activity: FragmentActivity?,
    map: GoogleMap,
    clusterManager: ClusterManager<Place>,
    private val viewModel: MainViewModel
) : DefaultClusterRenderer<Place>(
    activity,
    map,
    clusterManager
) {

    private var defaultMarker: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.pizza_marker)
    private val selectedMarker = BitmapDescriptorFactory.fromResource(R.drawable.pizza_marker_selected)

    init {
        clusterManager.renderer = this
    }

    override fun onBeforeClusterItemRendered(item: Place?, markerOptions: MarkerOptions?) {
        markerOptions?.apply {
            visible(true)
            title(item?.name)
            icon(markerOptions.icon)
        }
    }

    override fun onClusterItemRendered(clusterItem: Place?, marker: Marker?) {
        marker?.setIcon(getIcon(clusterItem!!))
        super.onClusterItemRendered(clusterItem, marker)
    }

    private fun getIcon(point: Place): BitmapDescriptor? {
        return when {
            point.id == viewModel.lastAndCurrentSelectedPlace.value?.second?.id -> selectedMarker
            else -> defaultMarker
        }
    }
}
