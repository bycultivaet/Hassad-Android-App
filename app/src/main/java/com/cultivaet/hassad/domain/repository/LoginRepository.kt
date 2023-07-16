package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.Facilitator

interface LoginRepository {
    suspend fun getFacilitator(phoneNumber: String): Resource<Facilitator>
}