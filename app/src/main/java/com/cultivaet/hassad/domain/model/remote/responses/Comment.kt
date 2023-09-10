package com.cultivaet.hassad.domain.model.remote.responses

data class Comment(
    val answer_id: Int,
    val consultant_id: Int,
    val media_type: String,
    val media_uuid: String,
    val text: String
)