package com.veresz.pizza.ui.map

import androidx.lifecycle.*
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.PizzaRepository
import com.veresz.pizza.ui.ViewState
import javax.inject.Inject
import kotlinx.coroutines.launch

class MapViewModel constructor(
    private val repository: PizzaRepository
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val pizzaPlacesLiveData by lazy {
        val liveData = MutableLiveData<List<Place>>()
        refresh()
        liveData
    }
    val pizzaPlaces: LiveData<List<Place>> = pizzaPlacesLiveData
    val viewState: LiveData<ViewState> = viewStateLiveData

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                viewStateLiveData.value = ViewState.Loading
                repository.getPizzaPlaces()
            }.onSuccess {
                viewStateLiveData.value = ViewState.Data
                pizzaPlacesLiveData.value = it.list.places
            }.onFailure {
                viewStateLiveData.value = ViewState.Error
            }
        }
    }

    class Factory @Inject constructor(
        private val repository: PizzaRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapViewModel(repository) as T
        }
    }
}
