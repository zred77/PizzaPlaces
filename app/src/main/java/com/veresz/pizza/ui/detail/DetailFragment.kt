package com.veresz.pizza.ui.detail

import android.animation.Animator
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.transition.Transition
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.veresz.pizza.R
import com.veresz.pizza.base.BaseFragment
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.Image
import com.veresz.pizza.model.Place
import javax.inject.Inject
import javax.inject.Named
import kotlinx.android.synthetic.main.detail_content.bookNow
import kotlinx.android.synthetic.main.detail_content.description
import kotlinx.android.synthetic.main.detail_content.distance
import kotlinx.android.synthetic.main.detail_content.divider1
import kotlinx.android.synthetic.main.detail_content.friendsLayout
import kotlinx.android.synthetic.main.detail_content.opening
import kotlinx.android.synthetic.main.detail_content.ratingBar
import kotlinx.android.synthetic.main.detail_content.readMore
import kotlinx.android.synthetic.main.fragment_detail.fab
import kotlinx.android.synthetic.main.fragment_detail.image
import kotlinx.android.synthetic.main.fragment_detail.rootLayout
import kotlinx.android.synthetic.main.fragment_detail.toolbar
import kotlinx.android.synthetic.main.fragment_detail.toolbar_layout
import kotlinx.android.synthetic.main.item_avatar.view.avatarImage

open class DetailFragment : BaseFragment() {

    private var fabAnimator: Animator? = null

    @field:[Inject Named("fragment")]
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal val viewModel by viewModels<DetailViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
        observeData()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            fab.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            insets
        }
    }

    fun getPizzaPlace(): Place {
        return arguments?.getParcelable(KEY_PLACE)!!
    }

    private fun observeData() {
        viewModel.place.observe(this, Observer {
            updateUI(it)
        })
        viewModel.friends.observe(this, Observer {
            addFriendsImages(viewModel.place.value, it)
        })
    }

    private fun addFriendsImages(place: Place?, friends: Map<String, Friend>) {
        var avatar: View
        friendsLayout.removeAllViews()
        place?.let { place ->
            place.friendIds.forEach { friendId ->
                avatar = View.inflate(context, R.layout.item_avatar, null)
                avatar.avatarImage.load(friends[friendId]?.avatarUrl ?: "") {
                    placeholder(R.drawable.avatar_placeholder)
                    error(R.drawable.avatar_placeholder)
                    transformations(CircleCropTransformation())
                    crossfade(true)
                }
                friends[friendId]?.let { friend ->
                    avatar.avatarImage.setOnClickListener {
                        Toast.makeText(context, friend.name, Toast.LENGTH_SHORT).show()
                    }
                }
                friendsLayout.addView(avatar)
            }
        }
    }

    private fun updateUI(place: Place) {
        with(place) {
            loadImage(this.images)
            ratingBar.rating = (0..5).random().toFloat()
            distance.text = getString(R.string.distance, distanceFrom.toString())
            description.text = getString(R.string.description)
            readMore.setOnClickListener {
                TransitionManager.beginDelayedTransition(rootLayout)
                if (description.maxLines == 5) {
                    description.maxLines = Integer.MAX_VALUE
                    divider1.isVisible = true
                    opening.isVisible = true
                } else {
                    description.maxLines = 5
                    divider1.isVisible = false
                    opening.isVisible = false
                }
            }
            opening.text = openingHours.joinToString("\n")
            bookNow.setOnClickListener {
                Toast.makeText(context, R.string.book_now, Toast.LENGTH_SHORT).show()
            }
            fab.setOnClickListener {
                Toast.makeText(context, R.string.mark_as_favourite, Toast.LENGTH_SHORT).show()
            }
            addFriendsImages(viewModel.place.value, viewModel.friends.value.orEmpty())
        }
    }

    private fun loadImage(images: List<Image>) {
        if (images.isNotEmpty()) {
            val url = Uri.parse(images[0].url)
            image.load(url) {
                allowHardware(false)
                placeholder(R.drawable.image_placeholder)
                error(R.drawable.image_placeholder)
            }
        }
    }

    private fun setTransitions() {
        sharedElementReturnTransition = null
        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(R.transition.sharedelement)
            .apply {
                duration = 350
                addListener(object : TransitionListener {
                    override fun onTransitionEnd(transition: Transition) {
                        if (isAdded) {
                            zoomInFab()
                            setToolbarScrimColor()
                        }
                    }

                    override fun onTransitionResume(transition: Transition) {
                    }

                    override fun onTransitionPause(transition: Transition) {
                    }

                    override fun onTransitionCancel(transition: Transition) {
                    }

                    override fun onTransitionStart(transition: Transition) {
                    }
                })
            }
        returnTransition = TransitionInflater.from(context).inflateTransition(R.transition.detail_out)
    }

    private fun setToolbarScrimColor() {
        image.drawable?.let {
            Palette.from(it.toBitmap()).generate { palette ->
                val defaultColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                val defaultColorDark = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                toolbar_layout.setContentScrimColor(palette?.getDominantColor(defaultColor) ?: defaultColor)
                toolbar_layout.setStatusBarScrimColor(palette?.getDarkMutedColor(defaultColor) ?: defaultColorDark)
            }
        }
    }

    override fun onDestroyView() {
        fabAnimator?.cancel()
        super.onDestroyView()
    }

    private fun zoomInFab() {
        fabAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            doOnStart {
                fab?.isVisible = true
            }
            addUpdateListener { animator ->
                fab?.apply {
                    val value = animator.animatedValue as Float
                    scaleX = value
                    scaleY = value
                    alpha = value
                }
            }
            start()
        }
    }

    companion object {

        const val KEY_PLACE = "key.place"
        fun newInstance(place: Place): Fragment {
            return DetailFragment().apply {
                arguments = bundleOf(
                    Pair(KEY_PLACE, place)
                )
            }
        }
    }
}
