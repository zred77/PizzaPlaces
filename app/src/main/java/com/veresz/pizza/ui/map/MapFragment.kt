package com.veresz.pizza.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.veresz.pizza.R
import com.veresz.pizza.model.Place
import com.veresz.pizza.ui.ViewState
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    private val AMSTERDAM = LatLng(52.386050, 4.888466)
    private val DEFAULT_ZOOMLEVEL = 13f

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var googleMap: GoogleMap? = null
    private val viewModel by viewModels<MapViewModel> { viewModelFactory }

    private var clusterManager: ClusterManager<Place>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(readyMap: GoogleMap) {
        googleMap = readyMap
        googleMap?.apply {
            ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
                this.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
                insets
            }
            uiSettings.apply {
                isRotateGesturesEnabled = false
                isTiltGesturesEnabled = false
                isMyLocationButtonEnabled = true
            }

            if (clusterManager == null) {
                clusterManager = ClusterManager(context, googleMap)
                clusterManager?.let {
                    it.setOnClusterItemClickListener {
                        val direction = MapFragmentDirections.actionMapFragmentToDetailFragment(it)
                        findNavController().navigate(direction)
                        true
                    }
                }
                ClusterRenderer(activity, this, clusterManager!!)
            }

            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
            moveMapCamera(AMSTERDAM, DEFAULT_ZOOMLEVEL)

            observeData()
        }
    }

    private fun observeData() {
        viewModel.pizzaPlaces.observe(this@MapFragment, Observer {
            clusterManager!!.clearItems()
            it.forEach { point ->
                clusterManager!!.addItem(point)
            }
            clusterManager!!.cluster()
        })
        viewModel.viewState.observe(this, Observer {
            when (it) {
                is ViewState.Error -> {
                    Toast.makeText(context, R.string.error_general, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun moveMapCamera(latLng: LatLng, zoom: Float) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    class ClusterRenderer(activity: FragmentActivity?, map: GoogleMap, clusterManager: ClusterManager<Place>) :
            DefaultClusterRenderer<Place>(activity, map, clusterManager) {

        init {
            clusterManager.renderer = this
        }

        override fun onBeforeClusterItemRendered(item: Place?, markerOptions: MarkerOptions?) {
            markerOptions?.apply {
                visible(true)
                title(item?.name)
            }
        }
    }
}
