package com.cultivaet.hassad.domain.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facilitator_answer")
class FacilitatorAnswer(
    @PrimaryKey
    var userId: Int,
    var formId: Int,
    var farmerId: Int,
    var geolocation: String,
    var answers: String,
    var type: String
)