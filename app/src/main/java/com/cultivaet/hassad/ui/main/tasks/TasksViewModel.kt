package com.cultivaet.hassad.ui.main.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.usecase.TasksUseCase
import com.cultivaet.hassad.ui.main.tasks.intent.TasksIntent
import com.cultivaet.hassad.ui.main.tasks.viewstate.TasksState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TasksViewModel(
    private val tasksUseCase: TasksUseCase
) : ViewModel() {
    val tasksIntent = Channel<TasksIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<TasksState>(TasksState.Idle)
    val state: StateFlow<TasksState> = _state

    internal var userId: Int = -1

    lateinit var taskDataItem: TaskDataItem

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            tasksIntent.consumeAsFlow().collect {
                when (it) {
                    is TasksIntent.FetchAllTasks -> getAllTasksById(userId)

                    is TasksIntent.UpdateTaskStatus -> {
                        updateTaskStatus(userId, taskDataItem.id, taskDataItem.isChecked)
                    }

                    is TasksIntent.FetchAllNotes -> getAllNotesById(userId)
                }
            }
        }
    }

    private fun getAllTasksById(id: Int) {
        viewModelScope.launch {
            _state.value = TasksState.Loading
            _state.value = when (val resource = tasksUseCase.getAllTasksById(id)) {
                is Resource.Success -> {
                    TasksState.Success(resource.data?.map { it.toTaskDataItem() })
                }

                is Resource.Error -> TasksState.Error(resource.error)
            }
        }
    }

    private fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ) {
        viewModelScope.launch {
            _state.value = TasksState.Loading
            _state.value =
                when (val resource = tasksUseCase.updateTaskStatus(facilitatorId, taskId, status)) {
                    is Resource.Success -> {
                        TasksState.Success(resource.data)
                    }

                    is Resource.Error -> TasksState.Error(resource.error)
                }
        }
    }

    private fun getAllNotesById(id: Int) {
        viewModelScope.launch {
            _state.value = TasksState.Loading
            _state.value = when (val resource = tasksUseCase.getAllNotesById(id)) {
                is Resource.Success -> {
                    TasksState.Success(resource.data?.map { it.toNoteDataItem() })
                }

                is Resource.Error -> TasksState.Error(resource.error)
            }
        }
    }
}