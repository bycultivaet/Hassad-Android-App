package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.TasksRepository

class TasksUseCase(private val repository: TasksRepository) {
    suspend fun userId() = repository.userId()

    suspend fun getAllTasksById(
        id: Int,
    ) = repository.getAllTasksById(id)

    suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ) = repository.updateTaskStatus(facilitatorId, taskId, status)
}