package com.cultivaet.hassad.ui.main.addfarmer.intent

sealed class AddFarmerIntent {
    object GetUserId : AddFarmerIntent()

    object FetchAllFarmers : AddFarmerIntent()

    object AddFarmer : AddFarmerIntent()
}