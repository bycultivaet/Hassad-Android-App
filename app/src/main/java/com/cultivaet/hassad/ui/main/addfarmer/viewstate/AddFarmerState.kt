package com.cultivaet.hassad.ui.main.addfarmer.viewstate

import com.cultivaet.hassad.domain.model.remote.responses.Farmer

sealed class AddFarmerState {
    object Idle : AddFarmerState()
    object Loading : AddFarmerState()
    data class Success(val farmers: List<Farmer>?) : AddFarmerState()
    data class Error(val error: String?) : AddFarmerState()
}