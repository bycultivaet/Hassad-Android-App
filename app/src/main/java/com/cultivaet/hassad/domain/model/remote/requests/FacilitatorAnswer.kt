package com.cultivaet.hassad.domain.model.remote.requests

data class FacilitatorAnswer(
    val answers: List<Answer>,
    val farmerId: Int,
    val formId: Int,
    val geolocation: String,
    val type: String,
    val userId: Int
)