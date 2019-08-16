package com.veresz.pizza.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.model.Place
import com.veresz.pizza.repository.PizzaRepository
import com.veresz.pizza.util.livedata
import javax.inject.Inject

class DetailViewModel(
    private val repository: PizzaRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    val place by savedState.livedata<Place>()

    class Factory @Inject constructor(
        private val place: Place,
        private val repository: PizzaRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailViewModel(repository, SavedStateHandle().apply {
                set("place", place)
            }) as T
        }
    }
}
