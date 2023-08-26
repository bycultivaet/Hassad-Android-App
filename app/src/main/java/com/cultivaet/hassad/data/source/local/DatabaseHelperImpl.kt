package com.cultivaet.hassad.data.source.local

import com.cultivaet.hassad.core.source.local.AppDatabase
import com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.local.Farmer

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun insertFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) =
        appDatabase.facilitatorAnswerDao().insert(facilitatorAnswer)

    override suspend fun deleteFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) =
        appDatabase.facilitatorAnswerDao().delete(facilitatorAnswer)

    override suspend fun getFacilitatorAnswers(): List<FacilitatorAnswer> =
        appDatabase.facilitatorAnswerDao().getFacilitatorAnswers()

    override suspend fun insertFarmer(farmer: Farmer) =
        appDatabase.farmerDao().insert(farmer)

    override suspend fun deleteFarmer(farmer: Farmer) =
        appDatabase.farmerDao().delete(farmer)

    override suspend fun getFarmers(): List<Farmer> =
        appDatabase.farmerDao().getFarmers()
}