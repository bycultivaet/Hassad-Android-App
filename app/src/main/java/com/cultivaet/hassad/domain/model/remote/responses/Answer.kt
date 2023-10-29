package com.cultivaet.hassad.domain.model.remote.responses

data class Answer(
    val ID: Int,
    val answers: List<AnswerDetails>,
    val farmerId: Int,
    val formId: Int,
    val geolocation: String,
    val type: String,
    val userId: Int
)