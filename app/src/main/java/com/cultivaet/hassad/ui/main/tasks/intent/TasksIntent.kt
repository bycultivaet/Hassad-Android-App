package com.cultivaet.hassad.ui.main.tasks.intent

sealed class TasksIntent {
    object GetUserId : TasksIntent()

    object FetchAllTasks : TasksIntent()

    object UpdateTaskStatus : TasksIntent()
}