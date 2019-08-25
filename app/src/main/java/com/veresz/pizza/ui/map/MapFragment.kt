package com.veresz.pizza.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.veresz.pizza.R
import com.veresz.pizza.model.Place
import com.veresz.pizza.ui.ViewState
import com.veresz.pizza.ui.main.MainViewModel
import com.veresz.pizza.ui.maplist.MapListFragment
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.synthetic.main.fragment_map.rootLayout
import kotlinx.android.synthetic.main.fragment_map.titleBar

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    private val AMSTERDAM = LatLng(52.386050, 4.888466)
    private val DEFAULT_ZOOMLEVEL = 13f
    private val ANIMATECAMERA_DURATION = 200

    @field:[Inject Named("fragment")]
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @field:[Inject Named("activity")]
    lateinit var activityViewModelFactory: ViewModelProvider.Factory
    private var googleMap: GoogleMap? = null
    private val viewModel by viewModels<MapViewModel> { viewModelFactory }
    private val activityViewModel by activityViewModels<MainViewModel> { activityViewModelFactory }

    private var clusterManager: ClusterManager<Place>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel.lastAndCurrentSelectedPlace.observe(this, Observer {
            highlightMarker(it)
            it.second?.let { place ->
                animateMapCamera(place.position, DEFAULT_ZOOMLEVEL)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        observeState()
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
                        activityViewModel.setSelectedPoint(it)
                        showMapList()
                        true
                    }
                }
                ClusterRenderer(activity, this, clusterManager!!, activityViewModel)
            }

            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
            moveMapCamera(AMSTERDAM, DEFAULT_ZOOMLEVEL)

            observeData()
        }
    }

    private fun showMapList() {
        with(requireFragmentManager()) {
            if (findFragmentByTag(MapListFragment.TAG) == null) {
                val fragment = MapListFragment.newInstance()
                commit {
                    addToBackStack(null)
                    add(R.id.nav_host_fragment, fragment, MapListFragment.TAG)
                }
            }
        }
    }

    private fun highlightMarker(selectedPair: Pair<Place?, Place?>) {
        selectedPair.first?.let {
            val p = viewModel.points[it.id]!!
            removePointFromClusterManager(p)
            addPointToClusterManager(p)
        }
        selectedPair.second?.let {
            val p = viewModel.points[it.id]!!
            removePointFromClusterManager(p)
            addPointToClusterManager(p)
        }
        clusterManager?.cluster()
    }

    private fun addPointToClusterManager(place: Place) {
        clusterManager?.addItem(place)
        viewModel.points[place.id] = place
    }

    private fun removePointFromClusterManager(place: Place) {
        clusterManager?.removeItem(place)
        viewModel.points.remove(place.id)
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(titleBar) { view, insets ->
            rootLayout.updatePadding(
                top = 0,
                bottom = 0
            )
            titleBar.updateLayoutParams<MarginLayoutParams> {
                topMargin = (insets.systemWindowInsetTop + resources.getDimension(R.dimen.spacing_default)).toInt()
            }
            insets
        }
    }

    private fun observeData() {
        activityViewModel.pizzaPlaces.observe(this@MapFragment, Observer {
            clusterManager!!.clearItems()
            it.forEach { point ->
                viewModel.points[point.id] = point
                clusterManager!!.addItem(point)
            }
            clusterManager!!.cluster()
        })
    }

    private fun observeState() {
        activityViewModel.viewState.observe(this, Observer {
            when (it) {
                is ViewState.Error -> {
                    Snackbar.make(rootLayout, R.string.error_general, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) {
                            activityViewModel.getAllPlaces(true)
                        }
                        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                        .show()
                }
            }
        })
    }

    private fun animateMapCamera(latLng: LatLng, zoom: Float) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), ANIMATECAMERA_DURATION, null)
    }

    private fun moveMapCamera(latLng: LatLng, zoom: Float) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    companion object {

        fun newInstance(): Fragment {
            return MapFragment()
        }
    }
}
