package com.cultivaet.hassad.ui.main.tasks.intent

sealed class TasksIntent {
    object FetchAllTasks : TasksIntent()

    object UpdateTaskStatus : TasksIntent()

    object FetchAllNotes : TasksIntent()

    object FetchAllVisits : TasksIntent()
}