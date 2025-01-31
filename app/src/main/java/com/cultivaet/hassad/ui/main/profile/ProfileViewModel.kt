package com.cultivaet.hassad.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.usecase.ProfileUseCase
import com.cultivaet.hassad.ui.main.profile.intent.ProfileIntent
import com.cultivaet.hassad.ui.main.profile.viewstate.ProfileState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ProfileViewModel(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    val profileIntent = Channel<ProfileIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state
    internal var userId: Int = -1

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            profileIntent.consumeAsFlow().collect {
                when (it) {
                    is ProfileIntent.FetchFacilitator -> getFacilitatorById(userId)
                }
            }
        }
    }

    private fun getFacilitatorById(id: Int) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            _state.value =
                when (val resource = profileUseCase.getFacilitator(id)) {
                    is Resource.Success -> {
                        val facilitator = resource.data
                        ProfileState.Success((facilitator?.toFacilitatorDataItem()))
                    }

                    is Resource.Error -> ProfileState.Error(resource.error)
                }
        }
    }
}