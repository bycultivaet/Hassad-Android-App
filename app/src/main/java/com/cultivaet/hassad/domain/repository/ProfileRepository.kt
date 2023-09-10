package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator

interface ProfileRepository {
    suspend fun getFacilitator(id: Int): Resource<Facilitator>
}