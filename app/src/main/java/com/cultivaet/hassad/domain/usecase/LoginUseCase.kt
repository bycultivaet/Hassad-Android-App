package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.model.remote.Facilitator
import com.cultivaet.hassad.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend fun getFacilitatorByPhoneNumber(phoneNumber: String): Facilitator =
        repository.getFacilitator(phoneNumber)
}