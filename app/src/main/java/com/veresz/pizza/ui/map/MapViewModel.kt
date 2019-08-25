package com.veresz.pizza.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.model.Place
import javax.inject.Inject

class MapViewModel : ViewModel() {

    var points: MutableMap<String, Place> = mutableMapOf()

    class Factory @Inject constructor() : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapViewModel() as T
        }
    }
}
