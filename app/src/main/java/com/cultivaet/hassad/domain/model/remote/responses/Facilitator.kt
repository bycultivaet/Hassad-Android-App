package com.cultivaet.hassad.domain.model.remote.responses

import com.cultivaet.hassad.ui.auth.model.FacilitatorDataItem

data class Facilitator(
    var ID: Int,
    var firstName: String? = null,
    var lastName: String? = null,
    var gender: String? = null,
    var age: Int,
    var university: String? = null,
    var major: String? = null,
    var gradYear: Int,
    var experience: Int,
    var hasVehicle: Boolean? = null,
    var address: String? = null,
    var phoneNumber: String? = null
) {
    fun toFacilitatorDataItem() = FacilitatorDataItem(
        id = ID,
        firstName = firstName.toString(),
        lastName = lastName.toString(),
        gender = gender.toString(),
        age = age,
        university = university.toString(),
        major = major.toString(),
        gradYear = gradYear,
        experience = experience,
        hasVehicle = hasVehicle == true,
        address = address.toString(),
        phoneNumber = phoneNumber.toString()
    )
}