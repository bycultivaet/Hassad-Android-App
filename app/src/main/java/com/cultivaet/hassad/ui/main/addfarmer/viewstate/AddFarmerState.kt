package com.cultivaet.hassad.ui.main.addfarmer.viewstate

import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem

sealed class AddFarmerState {
    object Idle : AddFarmerState()
    object Loading : AddFarmerState()
    data class Success<T>(val data: T?) : AddFarmerState()
    data class Error(val error: String?) : AddFarmerState()
}