package com.cultivaet.hassad.ui.auth.model

import android.content.Context
import com.cultivaet.hassad.R

data class FacilitatorDataItem(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val age: Int,
    val university: String,
    val major: String,
    val gradYear: Int,
    val experience: Int,
    val hasVehicle: Boolean,
    val address: String,
    val phoneNumber: String
) {
    fun answerOfHavingVehicle(context: Context?) =
        context?.getString(if (hasVehicle) R.string.yes else R.string.no)
}