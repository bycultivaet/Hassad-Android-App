package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.TasksRepository

class TasksUseCase(private val repository: TasksRepository) {
    suspend fun getAllTasksById(
        id: Int,
    ) = repository.getAllTasksById(id)

    suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ) = repository.updateTaskStatus(facilitatorId, taskId, status)

    suspend fun getAllNotesById(
        id: Int,
    ) = repository.getAllNotesById(id)

    suspend fun getFVVisitsByFacilitatorId(
        id: Int,
    ) = repository.getFVVisitsByFacilitatorId(id)

    suspend fun getFFSVisitsByFacilitatorId(
        id: Int,
        active: Boolean = true
    ) = repository.getFFSVisitsByFacilitatorId(id, active)
}