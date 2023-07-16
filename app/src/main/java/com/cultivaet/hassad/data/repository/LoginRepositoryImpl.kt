package com.cultivaet.hassad.data.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.Facilitator
import com.cultivaet.hassad.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val apiHelper: ApiHelper,
//    private val databaseHelper: DatabaseHelper
) : BaseRepository(), LoginRepository {
    override suspend fun getFacilitator(phoneNumber: String): Resource<Facilitator> {
        return safeApiCall { apiHelper.getFacilitator(phoneNumber) }

    }
}