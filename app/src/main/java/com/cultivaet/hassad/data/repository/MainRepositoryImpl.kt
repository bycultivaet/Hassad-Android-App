package com.cultivaet.hassad.data.repository

import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.domain.repository.MainRepository

class MainRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource
) : MainRepository {
    override suspend fun userLoggedOut() = preferencesDataSource.userLoggedOut()
}