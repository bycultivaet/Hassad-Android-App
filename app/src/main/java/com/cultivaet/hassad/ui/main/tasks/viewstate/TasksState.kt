package com.cultivaet.hassad.ui.main.tasks.viewstate

sealed class TasksState {
    object Idle : TasksState()
    object Loading : TasksState()
    data class Success<T>(val data: T?) : TasksState()
    data class Error(val error: String?) : TasksState()
}