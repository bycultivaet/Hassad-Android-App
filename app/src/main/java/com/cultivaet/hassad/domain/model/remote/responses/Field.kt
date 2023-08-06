package com.cultivaet.hassad.domain.model.remote.responses

data class Field(
    val name: String,
    val options: List<Option>,
    val placeholder: String,
    val required: Boolean,
    val type: String
)