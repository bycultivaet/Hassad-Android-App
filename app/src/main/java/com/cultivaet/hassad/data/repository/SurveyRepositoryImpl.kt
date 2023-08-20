package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.local.DatabaseHelper
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.repository.SurveyRepository

class SurveyRepositoryImpl(
    application: Application,
    private val preferencesDataSource: PreferencesDataSource,
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper
) : BaseRepository(application), SurveyRepository {
    override suspend fun userId() = preferencesDataSource.userId()

    override suspend fun getAllFarmersById(id: Int, filter: Boolean): Resource<List<Farmer>> =
        safeApiCall { apiHelper.getAllFarmersById(id, filter) }

    override suspend fun getFacilitatorForm(id: Int): Resource<Form> =
        safeApiCall { apiHelper.getFacilitatorForm(id) }

    override suspend fun submitFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer): Resource<com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer> =
        safeApiCall { apiHelper.submitFacilitatorAnswer(facilitatorAnswer) }

    override suspend fun insertFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    ) = databaseHelper.insertFacilitatorAnswer(facilitatorAnswer)

    override suspend fun deleteFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    ) = databaseHelper.deleteFacilitatorAnswer(facilitatorAnswer)

    override suspend fun getFacilitatorAnswers(): List<com.cultivaet.hassad.domain.model.local.FacilitatorAnswer> =
        databaseHelper.getFacilitatorAnswers()
}