package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {
    suspend fun userId(): Flow<Int?>

    suspend fun getAllFarmersById(id: Int, filter: Boolean): Resource<List<Farmer>>

    suspend fun getFarmerForm(id: Int): Resource<Form>
}