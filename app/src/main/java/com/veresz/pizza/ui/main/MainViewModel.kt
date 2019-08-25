package com.veresz.pizza.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.place.PlaceRepository
import com.veresz.pizza.ui.ViewState
import javax.inject.Inject
import kotlinx.coroutines.launch

class MainViewModel(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val pizzaPlacesLiveData by lazy {
        val liveData = MutableLiveData<List<Place>>()
        getAllPlaces()
        liveData
    }
    val pizzaPlaces: LiveData<List<Place>> = pizzaPlacesLiveData
    val lastAndCurrentSelectedPlace = MutableLiveData<Pair<Place?, Place?>>()
    val viewState: LiveData<ViewState> = viewStateLiveData

    fun getAllPlaces(refresh: Boolean = false) {
        viewModelScope.launch {
            runCatching {
                viewStateLiveData.value = ViewState.Loading
                placeRepository.getPizzaPlaces(refresh)
            }.onSuccess {
                viewStateLiveData.value = ViewState.Data
                pizzaPlacesLiveData.value = it
            }.onFailure {
                viewStateLiveData.value = ViewState.Error(it)
            }
        }
    }

    fun setSelectedPoint(selectedPoint: Place?) {
        lastAndCurrentSelectedPlace.postValue(Pair(lastAndCurrentSelectedPlace.value?.second, selectedPoint))
    }

    class Factory @Inject constructor(
        private val repository: PlaceRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}
