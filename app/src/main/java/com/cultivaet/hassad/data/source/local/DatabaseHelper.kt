package com.cultivaet.hassad.data.source.local

import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer

interface DatabaseHelper {
    suspend fun insertFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer)

    suspend fun deleteFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer)

    suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer>
}