package com.cultivaet.hassad.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer")
class Farmer(
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var gender: String,
    var age: Int,
    var address: String,
    var landArea: Double,
    var ownership: String,
    var geolocation: String,
    var ZeroDay: String,
    var cropType: String,
    var cropsHistory: String,
    var facilitatorId: Int,
    @PrimaryKey(autoGenerate = true)
    val farmerId: Int = 0
)