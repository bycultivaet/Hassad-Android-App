package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.domain.model.remote.Facilitator
import retrofit2.Response

interface ApiHelper {
    suspend fun getFacilitator(phoneNumber: String): Response<Facilitator>

    suspend fun getFacilitator(id: Int): Response<Facilitator>
}