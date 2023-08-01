package com.cultivaet.hassad.ui.main.farmers

data class FarmerDataItem(
    val id: Int,
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
    val facilitatorId: Int
) {
    val fullName = "$firstName $lastName"
}