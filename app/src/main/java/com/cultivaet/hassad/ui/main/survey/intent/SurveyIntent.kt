package com.cultivaet.hassad.ui.main.survey.intent

sealed class SurveyIntent {
    object GetUserId : SurveyIntent()

    object FetchAllFarmers : SurveyIntent()
}