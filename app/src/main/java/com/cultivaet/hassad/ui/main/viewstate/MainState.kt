package com.cultivaet.hassad.ui.main.viewstate

sealed class MainState {
    object Idle : MainState()

    object SuccessSurvey : MainState()

    object SuccessFarmers : MainState()
}
