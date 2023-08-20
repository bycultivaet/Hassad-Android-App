package com.cultivaet.hassad.ui.profile.intent

sealed class ProfileIntent {
    object GetUserId : ProfileIntent()

    object FetchFacilitator : ProfileIntent()
}