package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.ProfileRepository

class ProfileUseCase(private val repository: ProfileRepository) {
    suspend fun getFacilitator(id: Int) = repository.getFacilitator(id)
}