package com.cultivaet.hassad.core.source.remote

import com.cultivaet.hassad.core.util.Constants
import com.cultivaet.hassad.domain.model.remote.Facilitator
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(Constants.EndPoints.GetFacilitatorByPhoneNumber)
    suspend fun getFacilitatorByPhoneNumber(@Path("phone") phoneNumber: String): Facilitator
}