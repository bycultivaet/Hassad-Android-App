package com.cultivaet.hassad.ui.auth.intent

sealed class LoginIntent {
    object FetchFacilitator : LoginIntent()
}