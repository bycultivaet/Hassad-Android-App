package com.cultivaet.hassad.domain.model.remote.responses

data class Form(
    var ID: Int = -1,
    var description: String = "",
    var fields: List<Field>? = mutableListOf(),
    var name: String = ""
)