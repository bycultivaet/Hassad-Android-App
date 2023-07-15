package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.domain.model.remote.Facilitator

interface LoginRepository {
    suspend fun getFacilitator(phoneNumber: String): Facilitator
}