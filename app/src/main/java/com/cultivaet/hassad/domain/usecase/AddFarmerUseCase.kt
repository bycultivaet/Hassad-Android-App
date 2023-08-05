package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.AddFarmerRepository

class AddFarmerUseCase(private val repository: AddFarmerRepository) {
    suspend fun userId() = repository.userId()

    suspend fun getAllFarmersById(id: Int, filter: Boolean = false) =
        repository.getAllFarmersById(id, filter)

    suspend fun addFarmer(farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer) =
        repository.addFarmer(farmer)
}