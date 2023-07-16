package com.cultivaet.hassad.ui.auth.viewstate

import com.cultivaet.hassad.ui.auth.model.FacilitatorDataItem

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val facilitator: FacilitatorDataItem?) : LoginState()
    data class Error(val error: String?) : LoginState()
}