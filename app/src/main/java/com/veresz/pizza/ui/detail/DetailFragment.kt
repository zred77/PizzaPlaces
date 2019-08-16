package com.veresz.pizza.ui.detail

import android.animation.Animator
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.veresz.pizza.R
import com.veresz.pizza.model.Image
import com.veresz.pizza.model.Place
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : DaggerFragment() {

    private val anims = mutableSetOf<Animator>()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<DetailViewModel> { viewModelFactory }
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        observeData()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            rootLayout.updatePadding(
                    left = insets.systemWindowInsetLeft,
                    right = insets.systemWindowInsetRight,
                    bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }

    override fun onStop() {
        anims.forEach { it.cancel() }
        super.onStop()
    }

    fun getPizzaPlace(): Place {
        return args.place
    }

    private fun observeData() {
        viewModel.place.observe(this, Observer {
            updateUI(it)
        })
    }

    private fun updateUI(place: Place) {
        with(place) {
            loadImage(this.images)
        }
    }

    private fun loadImage(images: List<Image>) {
        if (images.isNotEmpty()) {
            val url = Uri.parse(images[0].url)
            image.load(url)
        }
    }
}
