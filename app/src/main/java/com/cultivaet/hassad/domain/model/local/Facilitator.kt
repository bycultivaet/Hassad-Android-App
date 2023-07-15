package com.cultivaet.hassad.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facilitator")
class Facilitator(
    @PrimaryKey
    var id: Int,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var age: Int,
    var university: String,
    var major: String,
    var gradYear: Int,
    var experience: Int,
    var hasVehicle: Boolean,
    var address: String,
    var phoneNumber: String
)