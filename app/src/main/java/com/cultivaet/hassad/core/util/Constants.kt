package com.cultivaet.hassad.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cultivaet.hassad.BuildConfig

object Constants {
    val cacheMedia: HashMap<String, String> = hashMapOf()

    object EndPoints {
        const val GetFacilitatorByPhoneNumber = "api/facilitator/search/{phone}"
        const val GetFacilitatorById = "api/facilitator/{id}"

        const val GetAllFarmersById = "api/facilitator/{id}/farmers/"
        const val PostFarmer = "api/farmer"

        const val GetFacilitatorForm = "api/facilitator/{id}/form"
        const val PostSubmitFacilitatorAnswer = "api/facilitator/answer"

        const val PostImage = "api/images"

        const val GetAllTasksById = "api/facilitator/{id}/tasks"
        const val PATCH_TASK = "api/facilitator/{facilitator_id}/task/{task_id}"

        const val GetAllNotesById = "api/facilitator/{id}/notes"

        const val GetAllCommentsByFacilitatorId = "api/facilitator/{id}/farmers/comments"
        const val GetAnswerById = "api/answer/{id}"

        const val GetFileByUUID = "api/images/{uuid}"

        const val GetFVVisitsByFacilitatorId = "api/facilitator/{id}/fv/visits"
        const val GetFFSVisitsByFacilitatorId = "api/facilitator/{id}/ffs/visits"
    }

    object PreferenceKeys {
        val USER_ID = intPreferencesKey("id")
        val IS_LOGGED = booleanPreferencesKey("is_logged")
        val FACILITATOR_FORM = stringPreferencesKey("facilitator_form")
    }

    object Database {
        const val NAME = "${BuildConfig.APPLICATION_ID}.db"
    }
}