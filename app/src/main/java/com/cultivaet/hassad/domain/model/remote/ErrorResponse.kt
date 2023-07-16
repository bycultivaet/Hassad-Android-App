package com.cultivaet.hassad.domain.model.remote

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") var error: String? = null
)