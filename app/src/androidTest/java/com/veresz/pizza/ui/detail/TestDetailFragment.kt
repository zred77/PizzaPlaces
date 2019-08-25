package com.veresz.pizza.ui.detail

import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.friend.FriendRepository

class TestDetailFragment(
    private val place: Place,
    private val friendRepository: FriendRepository
) : DetailFragment() {

    override fun injectMembers() {
        this.viewModelFactory = DetailViewModel.Factory(place, friendRepository)
    }
}
