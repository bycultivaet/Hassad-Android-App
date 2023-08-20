package com.cultivaet.hassad.ui.profile.viewstate

import com.cultivaet.hassad.ui.auth.model.FacilitatorDataItem

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val facilitator: FacilitatorDataItem?) : ProfileState()
    data class Error(val error: String?) : ProfileState()
}