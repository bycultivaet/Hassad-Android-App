package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator
import com.cultivaet.hassad.domain.repository.LoginRepository

class LoginRepositoryImpl(
    application: Application,
    private val apiHelper: ApiHelper,
    private val preferencesDataSource: PreferencesDataSource
) : BaseRepository(application), LoginRepository {
    override suspend fun getFacilitator(phoneNumber: String): Resource<Facilitator> =
        safeApiCall { apiHelper.getFacilitator(phoneNumber) }

    override suspend fun userLoggedIn(userId: Int) = preferencesDataSource.userLoggedIn(userId)
}