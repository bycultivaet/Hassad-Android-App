package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.core.source.remote.ApiService
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getFacilitator(phoneNumber: String) =
        apiService.getFacilitatorByPhoneNumber(phoneNumber)

    override suspend fun getFacilitator(id: Int) = apiService.getFacilitatorById(id)
    override suspend fun getAllFarmersById(id: Int, filter: Boolean): Response<List<Farmer>> =
        apiService.getAllFarmersById(id, filter)

    override suspend fun addFarmer(farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer): Response<Farmer> =
        apiService.addFarmer(farmer)

    override suspend fun getFacilitatorForm(id: Int): Response<Form> = apiService.getFacilitatorForm(id)
}