package com.cultivaet.hassad.ui.main.survey.intent

sealed class SurveyIntent {
    object FetchAllFarmers : SurveyIntent()

    object FetchFacilitatorForm : SurveyIntent()

    object SubmitFacilitatorAnswer : SurveyIntent()

    object InsertFacilitatorAnswerOffline : SurveyIntent()

    object SetFacilitatorForm : SurveyIntent()

    object GetFacilitatorForm : SurveyIntent()
}