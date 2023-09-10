package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.FarmersRepository

class FarmersUseCase(private val repository: FarmersRepository) {
    suspend fun getAllFarmersById(id: Int, filter: Boolean = false) =
        repository.getAllFarmersById(id, filter)
}