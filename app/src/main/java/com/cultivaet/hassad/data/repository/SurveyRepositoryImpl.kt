package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.repository.SurveyRepository

class SurveyRepositoryImpl(
    application: Application,
    private val apiHelper: ApiHelper,
    private val preferencesDataSource: PreferencesDataSource
) : BaseRepository(application), SurveyRepository {
    override suspend fun userId() = preferencesDataSource.userId()

    override suspend fun getAllFarmersById(id: Int, filter: Boolean): Resource<List<Farmer>> =
        safeApiCall { apiHelper.getAllFarmersById(id, filter) }

    override suspend fun getFacilitatorForm(id: Int): Resource<Form> =
        safeApiCall { apiHelper.getFacilitatorForm(id) }
}