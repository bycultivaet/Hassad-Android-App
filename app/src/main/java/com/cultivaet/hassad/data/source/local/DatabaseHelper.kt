package com.cultivaet.hassad.data.source.local

import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.local.Farmer

interface DatabaseHelper {
    suspend fun insertFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer)

    suspend fun deleteFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer)

    suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer>

    suspend fun insertFarmer(farmer: Farmer)

    suspend fun deleteFarmer(farmer: Farmer)

    suspend fun getFarmers(): List<Farmer>
}