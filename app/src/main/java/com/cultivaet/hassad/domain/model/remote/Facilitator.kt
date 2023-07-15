package com.cultivaet.hassad.domain.model.remote

import com.cultivaet.hassad.ui.auth.model.FacilitatorDataItem
import com.google.gson.annotations.SerializedName

data class Facilitator(
    @SerializedName("ID") var id: Int,
    @SerializedName("CreatedAt") var createdAt: String? = null,
    @SerializedName("UpdatedAt") var updatedAt: String? = null,
    @SerializedName("DeletedAt") var deletedAt: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("age") var age: Int,
    @SerializedName("university") var university: String? = null,
    @SerializedName("major") var major: String? = null,
    @SerializedName("gradYear") var gradYear: Int,
    @SerializedName("experience") var experience: Int,
    @SerializedName("hasVehicle") var hasVehicle: Boolean? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("phoneNumber") var phoneNumber: String? = null
) {
    fun toFacilitatorDataItem() = FacilitatorDataItem(
        id = id,
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