package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.Facilitator
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun userId(): Flow<Int?>

    suspend fun getFacilitator(id: Int): Resource<Facilitator>
}