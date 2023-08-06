package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator
import com.cultivaet.hassad.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    application: Application,
    private val apiHelper: ApiHelper,
    private val preferencesDataSource: PreferencesDataSource
) : BaseRepository(application), ProfileRepository {
    override suspend fun userId() = preferencesDataSource.userId()

    override suspend fun getFacilitator(id: Int): Resource<Facilitator> =
        safeApiCall { apiHelper.getFacilitator(id) }
}