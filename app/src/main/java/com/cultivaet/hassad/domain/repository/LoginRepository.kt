package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator

interface LoginRepository {
    suspend fun getFacilitator(phoneNumber: String): Resource<Facilitator>

    suspend fun userLoggedIn(userId: Int)
}