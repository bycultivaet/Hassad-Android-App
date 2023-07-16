package com.cultivaet.hassad.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.usecase.LoginUseCase
import com.cultivaet.hassad.ui.auth.intent.LoginIntent
import com.cultivaet.hassad.ui.auth.viewstate.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state
    lateinit var phoneNumber: String

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            loginIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.FetchFacilitator -> getFacilitatorByPhoneNumber()
                }
            }
        }
    }

    private fun getFacilitatorByPhoneNumber() {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value = when (val resource = loginUseCase.getFacilitatorByPhoneNumber(phoneNumber)) {
                    is Resource.Success -> LoginState.Success((resource.data?.toFacilitatorDataItem()))
                    is Resource.Error -> LoginState.Error(resource.error)
                }
        }
    }
}