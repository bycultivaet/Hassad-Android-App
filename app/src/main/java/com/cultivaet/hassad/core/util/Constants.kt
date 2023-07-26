package com.cultivaet.hassad.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.cultivaet.hassad.BuildConfig

object Constants {
    object EndPoints {
        const val GetFacilitatorByPhoneNumber = "api/facilitator/search/{phone}"
        const val GetFacilitatorById = "api/facilitator/{id}"

        const val GetAllFarmersById = "api/facilitator/{id}/farmers/"
        const val PostFarmer = "api/farmer"
    }

    object PreferenceKeys {
        val USER_ID = intPreferencesKey("id")
        val IS_LOGGED = booleanPreferencesKey("is_logged")
    }

    object Database {
        const val NAME = "${BuildConfig.APPLICATION_ID}.db"
    }
}