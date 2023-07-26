package com.cultivaet.hassad.domain.model.remote.responses

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
)