package com.cultivaet.hassad.ui.main.viewstate

sealed class MainState {
    object Idle : MainState()
    object Success : MainState()
}
