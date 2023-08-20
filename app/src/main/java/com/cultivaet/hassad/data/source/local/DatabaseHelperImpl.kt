package com.cultivaet.hassad.data.source.local

import com.cultivaet.hassad.core.source.local.AppDatabase
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun insertFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) =
        appDatabase.facilitatorAnswerDao().insert(facilitatorAnswer)

    override suspend fun deleteFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) =
        appDatabase.facilitatorAnswerDao().delete(facilitatorAnswer)

    override suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer> =
        appDatabase.facilitatorAnswerDao().getFacilitatorAnswers()
}