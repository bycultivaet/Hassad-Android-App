package com.cultivaet.hassad.ui.main.intent

sealed class MainIntent {
    // -------------- Facilitator for caching --------------
    object SubmitOfflineFacilitatorAnswersList : MainIntent()

    // -------------- Farmer for caching --------------
    object SubmitOfflineFarmersList : MainIntent()
}