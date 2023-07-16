package com.cultivaet.hassad.data.repository

import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.domain.repository.SplashRepository

class SplashRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource
) : SplashRepository {
    override suspend fun isLoggedIn() = preferencesDataSource.isLoggedIn()
}