package com.cultivaet.hassad.core.source.local.datastore

import kotlinx.coroutines.flow.Flow

interface PreferencesDataSource {
    suspend fun userLoggedIn(userId: Int)

    fun userId(): Flow<Int?>

    fun isLoggedIn(): Flow<Boolean?>

    suspend fun userLoggedOut()

    suspend fun setFacilitatorForm(facilitatorFormJson: String)

    suspend fun getFacilitatorForm(): Flow<String?>
}