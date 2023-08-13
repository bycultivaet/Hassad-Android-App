package com.cultivaet.hassad.domain.model.remote.requests

data class FacilitatorAnswer(
    var userId: Int = -1,
    var formId: Int = -1,
    var farmerId: Int = -1,
    var geolocation: String = "",
    var answers: MutableList<Answer> = mutableListOf(),
    var type: String = "facilitator"
)