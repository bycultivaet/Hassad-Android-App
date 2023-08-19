package com.cultivaet.hassad.ui.main.farmers.viewstate

import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem

sealed class FarmersState {
    object Idle : FarmersState()
    object Loading : FarmersState()
    data class Success(val data: List<FarmerDataItem>?) : FarmersState()
    data class Error(val error: String?) : FarmersState()
}