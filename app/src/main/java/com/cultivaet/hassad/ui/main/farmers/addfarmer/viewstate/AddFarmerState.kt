package com.cultivaet.hassad.ui.main.farmers.addfarmer.viewstate

import com.cultivaet.hassad.domain.model.remote.responses.Farmer

sealed class AddFarmerState {
    object Idle : AddFarmerState()
    object Loading : AddFarmerState()
    data class Success(val data: Farmer?) : AddFarmerState()
    data class Error(val error: String?) : AddFarmerState()
}