package com.veresz.pizza.ui.maplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.veresz.pizza.R
import com.veresz.pizza.base.BaseFragment
import com.veresz.pizza.ui.detail.DetailFragment
import com.veresz.pizza.ui.main.MainViewModel
import com.veresz.pizza.util.SnapHelperOneByOne
import com.veresz.pizza.util.attachSnapHelperWithListener
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.synthetic.main.fragment_map_list.bottomSheet
import kotlinx.android.synthetic.main.fragment_map_list.recyclerView
import kotlinx.android.synthetic.main.fragment_map_list.rootLayout

class MapListFragment : BaseFragment() {

    @field:[Inject Named("activity")]
    lateinit var activityViewModelFactory: ViewModelProvider.Factory

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>
    private lateinit var snapHelper: SnapHelperOneByOne
    private lateinit var adapter: MapListAdapter
    private val activityViewModel by activityViewModels<MainViewModel> { activityViewModelFactory }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityViewModel.pizzaPlaces.observe(this, Observer {
            adapter.submitList(it)
            val scrollPosition = activityViewModel.pizzaPlaces.value?.indexOf(activityViewModel.lastAndCurrentSelectedPlace.value?.second) ?: -1
            recyclerView.scrollToPosition(scrollPosition)
            snapToPositon(scrollPosition)
        })
        activityViewModel.lastAndCurrentSelectedPlace.observe(this, Observer {
            val scrollPosition = activityViewModel.pizzaPlaces.value?.indexOf(it?.second) ?: -1
            recyclerView.scrollToPosition(scrollPosition)
            snapToPositon(scrollPosition)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheet()
        setWindowInsets()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.isNestedScrollingEnabled = false
        adapter = MapListAdapter { view, item ->
            fragmentManager?.commit {
                view.apply {
                    ViewCompat.setTransitionName(this, "background")
                    addSharedElement(this, "background")
                }
                view.findViewById<View>(R.id.image).apply {
                    ViewCompat.setTransitionName(this, "image")
                    addSharedElement(this, "image")
                }
                view.findViewById<View>(R.id.name).apply {
                    ViewCompat.setTransitionName(this, "name")
                    addSharedElement(this, "name")
                }
                view.findViewById<View>(R.id.address).apply {
                    ViewCompat.setTransitionName(this, "address")
                    addSharedElement(this, "address")
                }
                val fragment = DetailFragment.newInstance(item)
                addToBackStack(null)
                setCustomAnimations(0, android.R.anim.fade_out)
                replace(R.id.nav_host_fragment, fragment)
            }
        }
        recyclerView.adapter = adapter
        snapHelper = SnapHelperOneByOne()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.attachSnapHelperWithListener(snapHelper) {
            activityViewModel.setSelectedPoint(adapter.getItemById(it))
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityViewModel.setSelectedPoint(null)
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            recyclerView.updatePadding(
                top = 0,
                bottom = (insets.systemWindowInsetBottom + resources.getDimension(R.dimen.spacing_default)).toInt()
            )
            insets
        }
    }

    private fun setBottomSheet() {
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    fragmentManager?.popBackStack()
                }
            }
        })
    }

    private fun setTransitions() {
        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_bottom)
    }

    private fun snapToPositon(scrollPosition: Int) {
        recyclerView.post {
            recyclerView?.layoutManager?.let {
                val view = it.findViewByPosition(scrollPosition) ?: return@post
                val snapDistance = snapHelper.calculateDistanceToFinalSnap(it, view)
                if (snapDistance!![0] != 0 || snapDistance[1] != 0) {
                    recyclerView.smoothScrollBy(snapDistance[0], snapDistance[1])
                }
            }
        }
    }

    companion object {

        val TAG = MapListFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return MapListFragment()
        }
    }
}
