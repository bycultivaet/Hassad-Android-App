package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Farmer

interface FarmersRepository {
    suspend fun getAllFarmersById(id: Int, filter: Boolean): Resource<List<Farmer>>
}