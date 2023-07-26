package com.cultivaet.hassad.domain.model.remote.requests

data class Farmer(
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var gender: String? = null,
    var age: Int? = null,
    var address: String? = null,
    var landArea: Double? = null,
    var ownership: String? = null,
    var geolocation: String? = null,
    var currentYield: String? = null,
    var ZeroDay: String? = null,
    var cropType: String? = null,
    var cropsHistory: String? = null,
    var facilitatorId: Int? = null
)