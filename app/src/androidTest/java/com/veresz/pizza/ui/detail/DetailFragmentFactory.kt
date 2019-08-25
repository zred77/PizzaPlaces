package com.veresz.pizza.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.friend.FriendRepository

class DetailFragmentFactory(
    private val place: Place,
    private val friendRepository: FriendRepository,
    private val args: Bundle?
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val clazz = loadFragmentClass(classLoader, className)

        val fragment: Fragment?
        if (clazz == TestDetailFragment::class.java) {
            fragment = TestDetailFragment(place, friendRepository)
        } else {
            return super.instantiate(classLoader, className)
        }

        if (args != null) {
            fragment.arguments = args
        }

        return fragment
    }
}
