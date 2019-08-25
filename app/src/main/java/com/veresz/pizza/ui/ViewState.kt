package com.veresz.pizza.ui

sealed class ViewState {

    object Data : ViewState()
    class Error(val throwable: Throwable) : ViewState()
    object Loading : ViewState()
}
