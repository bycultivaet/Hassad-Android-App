package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Farmer

interface AddFarmerRepository {
    suspend fun getAllFarmersById(id: Int, filter: Boolean): Resource<List<Farmer>>

    suspend fun addFarmer(
        farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer
    ): Resource<Farmer>

    suspend fun insertFarmer(
        farmer: com.cultivaet.hassad.domain.model.local.Farmer
    )
}