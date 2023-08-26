package com.cultivaet.hassad.ui.main.farmers.addfarmer.intent

sealed class AddFarmerIntent {
    object GetUserId : AddFarmerIntent()

    object AddFarmer : AddFarmerIntent()

    object InsertFarmerOffline : AddFarmerIntent()
}