package com.cultivaet.hassad.ui.main.content.viewstate

sealed class ContentState {
    object Idle : ContentState()
    object Loading : ContentState()
    data class Success<T>(val data: T?) : ContentState()
    data class Error(val error: String?) : ContentState()
}