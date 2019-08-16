package com.veresz.pizza.ui

sealed class ViewState {

    object Data : ViewState()
    object Error : ViewState()
    object Loading : ViewState()
}
