package com.cultivaet.hassad.domain.model.remote.requests

data class Farmer(
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var gender: String = "",
    var age: Int = -1,
    var address: String = "",
    var landArea: Double = 0.0,
    var ownership: String = "",
    var geolocation: String = "",
    var ZeroDay: String = "",
    var cropType: String = "",
    var cropsHistory: String = "",
    var facilitatorId: Int = -1
)