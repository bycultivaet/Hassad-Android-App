package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Note
import com.cultivaet.hassad.domain.model.remote.responses.Task
import com.cultivaet.hassad.domain.model.remote.responses.UpdateStatus
import com.cultivaet.hassad.domain.model.remote.responses.Visit

interface TasksRepository {
    suspend fun getAllTasksById(id: Int): Resource<List<Task>>

    suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ): Resource<UpdateStatus>

    suspend fun getAllNotesById(id: Int): Resource<List<Note>>

    suspend fun getFVVisitsByFacilitatorId(id: Int): Resource<List<Visit>>

    suspend fun getFFSVisitsByFacilitatorId(id: Int, active: Boolean): Resource<List<Visit>>
}