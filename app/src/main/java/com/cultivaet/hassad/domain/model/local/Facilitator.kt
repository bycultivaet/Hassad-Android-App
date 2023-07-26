package com.cultivaet.hassad.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facilitator")
class Facilitator(
    @PrimaryKey
    var id: Long,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var age: Long,
    var university: String,
    var major: String,
    var gradYear: Long,
    var experience: Long,
    var hasVehicle: Boolean,
    var address: String,
    var phoneNumber: String
)