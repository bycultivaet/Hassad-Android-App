package com.cultivaet.hassad.data.source.local

import com.cultivaet.hassad.core.source.local.AppDatabase
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer

class DatabaseHelperImpl() : DatabaseHelper {
    override suspend fun insertFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) {
    }

    override suspend fun deleteFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) {
    }

    override suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer> {
        return listOf()
    }
}