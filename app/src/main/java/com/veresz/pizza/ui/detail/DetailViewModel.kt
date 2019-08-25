package com.veresz.pizza.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.veresz.pizza.model.Friend
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.friend.FriendRepository
import com.veresz.pizza.ui.ViewState
import com.veresz.pizza.util.livedata
import javax.inject.Inject
import kotlinx.coroutines.launch

class DetailViewModel(
    private val friendRepository: FriendRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    val place by savedState.livedata<Place>()
    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val friendsLiveData by lazy {
        val liveData = MutableLiveData<Map<String, Friend>>()
        getFriends()
        liveData
    }
    val friends: LiveData<Map<String, Friend>> = friendsLiveData
    val viewState: LiveData<ViewState> = viewStateLiveData

    private fun getFriends(refresh: Boolean = false) {
        viewModelScope.launch {
            runCatching {
                viewStateLiveData.value = ViewState.Loading
                friendRepository.getFriends(refresh)
            }.onSuccess {
                viewStateLiveData.value = ViewState.Data
                friendsLiveData.value = it.associateBy { friends -> friends.id.toString() }
            }.onFailure {
                viewStateLiveData.value = ViewState.Error(it)
            }
        }
    }

    class Factory @Inject constructor(
        private val place: Place,
        private val friendRepository: FriendRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(friendRepository, SavedStateHandle().apply {
                set("place", place)
            }) as T
        }
    }
}
