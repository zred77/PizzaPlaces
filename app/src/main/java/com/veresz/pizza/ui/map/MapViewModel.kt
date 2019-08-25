package com.veresz.pizza.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veresz.pizza.model.Place
import com.veresz.pizza.ui.ViewState
import javax.inject.Inject

class MapViewModel : ViewModel() {

    var points: MutableMap<String, Place> = mutableMapOf()

    private val viewStateLiveData = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = viewStateLiveData

    class Factory @Inject constructor() : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MapViewModel() as T
        }
    }
}
