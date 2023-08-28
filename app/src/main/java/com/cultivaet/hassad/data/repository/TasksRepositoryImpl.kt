package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Task
import com.cultivaet.hassad.domain.model.remote.responses.UpdateStatus
import com.cultivaet.hassad.domain.repository.TasksRepository

class TasksRepositoryImpl(
    application: Application,
    private val preferencesDataSource: PreferencesDataSource,
    private val apiHelper: ApiHelper,
) : BaseRepository(application), TasksRepository {
    override suspend fun userId() = preferencesDataSource.userId()

    override suspend fun getAllTasksById(id: Int): Resource<List<Task>> =
        safeApiCall { apiHelper.getAllTasksById(id) }

    override suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ): Resource<UpdateStatus> =
        safeApiCall { apiHelper.updateTaskStatus(facilitatorId, taskId, status) }
}