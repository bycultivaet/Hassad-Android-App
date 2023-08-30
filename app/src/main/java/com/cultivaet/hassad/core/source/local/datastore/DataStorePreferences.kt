package com.cultivaet.hassad.core.source.local.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.cultivaet.hassad.BuildConfig
import com.cultivaet.hassad.core.util.Constants.PreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.APPLICATION_ID)

class DataStorePreferences(private val application: Application) : PreferencesDataSource {
    override suspend fun userLoggedIn(userId: Int) {
        application.applicationContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ID] = userId
            preferences[PreferenceKeys.IS_LOGGED] = true
            preferences[PreferenceKeys.FACILITATOR_FORM] = ""
        }
    }

    override fun userId(): Flow<Int?> {
        return application.applicationContext.dataStore.data.map { preference -> preference[PreferenceKeys.USER_ID] }
    }

    override fun isLoggedIn(): Flow<Boolean?> {
        return application.applicationContext.dataStore.data.map { preference -> preference[PreferenceKeys.IS_LOGGED] }
    }

    override suspend fun userLoggedOut() {
        application.applicationContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_LOGGED] = false
            preferences[PreferenceKeys.FACILITATOR_FORM] = ""
        }
    }

    override suspend fun setFacilitatorForm(facilitatorFormJson: String) {
        application.applicationContext.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FACILITATOR_FORM] = facilitatorFormJson
        }
    }

    override suspend fun getFacilitatorForm(): Flow<String?> {
        return application.applicationContext.dataStore.data.map { preference -> preference[PreferenceKeys.FACILITATOR_FORM] }
    }
}