package com.cultivaet.hassad.domain.model.remote.responses

import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem

data class Farmer(
    val ID: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val gender: String,
    val age: Int,
    val address: String,
    val landArea: Double,
    val ownership: String,
    val geolocation: String,
    val zeroDay: String,
    val cropType: String,
    val cropsHistory: String,
    val facilitatorId: Int,
) {
    fun toFarmerDataItem() = FarmerDataItem(
        id = ID,
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        gender = gender,
        age = age,
        address = address,
        landArea = landArea,
        ownership = ownership,
        geolocation = geolocation,
        zeroDay = zeroDay,
        cropType = cropType,
        cropsHistory = cropsHistory,
        facilitatorId = facilitatorId,
    )
}