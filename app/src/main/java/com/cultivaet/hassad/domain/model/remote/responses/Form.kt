package com.cultivaet.hassad.domain.model.remote.responses

data class Form(
    val ID: Int,
    val description: String,
    val fields: List<Field>,
    val name: String
)