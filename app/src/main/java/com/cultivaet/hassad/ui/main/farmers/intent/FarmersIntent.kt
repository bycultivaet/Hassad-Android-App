package com.cultivaet.hassad.ui.main.farmers.intent

sealed class FarmersIntent {
    object GetUserId : FarmersIntent()

    object FetchAllFarmers : FarmersIntent()
}