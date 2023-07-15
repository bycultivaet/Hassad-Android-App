package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.domain.model.remote.Facilitator

interface ApiHelper {
    suspend fun getFacilitator(phoneNumber: String): Facilitator
}