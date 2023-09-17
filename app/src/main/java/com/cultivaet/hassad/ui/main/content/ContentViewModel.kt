package com.cultivaet.hassad.ui.main.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.usecase.ContentUseCase
import com.cultivaet.hassad.ui.main.content.intent.ContentIntent
import com.cultivaet.hassad.ui.main.content.viewstate.ContentState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ContentViewModel(
    private val contentUseCase: ContentUseCase
) : ViewModel() {
    val contentIntent = Channel<ContentIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<ContentState>(ContentState.Idle)
    val state: StateFlow<ContentState> = _state

    internal var userId: Int = -1

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            contentIntent.consumeAsFlow().collect {
                when (it) {
                    is ContentIntent.FetchAllComments -> getAllCommentsByFacilitatorId(userId)
                }
            }
        }
    }

    private fun getAllCommentsByFacilitatorId(id: Int) {
        viewModelScope.launch {
            _state.value = ContentState.Loading
            _state.value = when (val resource = contentUseCase.getAllCommentsByFacilitatorId(id)) {
                is Resource.Success -> {
                    ContentState.Success(resource.data?.map { it.toCommentDataItem() })
                }

                is Resource.Error -> ContentState.Error(resource.error)
            }
        }
    }
}