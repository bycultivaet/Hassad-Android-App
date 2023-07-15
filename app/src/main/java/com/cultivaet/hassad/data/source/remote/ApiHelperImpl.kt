package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.core.source.remote.ApiService

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getFacilitator(phoneNumber: String) = apiService.getFacilitatorByPhoneNumber(phoneNumber)
}