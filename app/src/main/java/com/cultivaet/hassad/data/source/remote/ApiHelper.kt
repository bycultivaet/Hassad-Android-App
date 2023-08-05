package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.domain.model.remote.responses.Facilitator
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import retrofit2.Response

interface ApiHelper {
    suspend fun getFacilitator(phoneNumber: String): Response<Facilitator>

    suspend fun getFacilitator(id: Int): Response<Facilitator>

    suspend fun getAllFarmersById(id: Int, filter: Boolean): Response<List<Farmer>>

    suspend fun addFarmer(farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer): Response<Farmer>
}