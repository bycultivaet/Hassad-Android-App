package com.cultivaet.hassad.data.repository

import com.cultivaet.hassad.data.source.local.DatabaseHelper
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val apiHelper: ApiHelper,
) : LoginRepository {
    override suspend fun getFacilitator(phoneNumber: String) = apiHelper.getFacilitator(phoneNumber)
}