package com.cultivaet.hassad.ui.main.profile.intent

sealed class ProfileIntent {
    object GetUserId : ProfileIntent()

    object FetchFacilitator : ProfileIntent()
}