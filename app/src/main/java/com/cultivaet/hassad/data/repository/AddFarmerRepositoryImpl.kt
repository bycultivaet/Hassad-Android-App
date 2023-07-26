package com.cultivaet.hassad.data.repository

import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.repository.AddFarmerRepository

class AddFarmerRepositoryImpl(
    private val apiHelper: ApiHelper,
    private val preferencesDataSource: PreferencesDataSource
) : BaseRepository(), AddFarmerRepository {
    override suspend fun userId() = preferencesDataSource.userId()

    override suspend fun getAllFarmersById(id: Int): Resource<List<Farmer>> =
        safeApiCall { apiHelper.getAllFarmersById(id) }

    override suspend fun addFarmer(farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer): Resource<Farmer> =
        safeApiCall { apiHelper.addFarmer(farmer) }
}