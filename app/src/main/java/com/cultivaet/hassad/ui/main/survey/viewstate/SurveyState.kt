package com.cultivaet.hassad.ui.main.survey.viewstate

sealed class SurveyState {
    object Idle : SurveyState()
    object Loading : SurveyState()
    data class Success<T>(val data: T?) : SurveyState()
    data class Error(val error: String?) : SurveyState()
}