package com.cultivaet.hassad.ui.auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.R
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
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class LoginViewModel(
    private val application: Application,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val loginIntent = Channel<LoginIntent>(Channel.UNLIMITED)
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
                    is LoginIntent.FetchFacilitator -> getFacilitatorByPhoneNumber(phoneNumber)
                }
            }
        }
    }

    private fun getFacilitatorByPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            _state.value =
                when (val resource = loginUseCase.getFacilitatorByPhoneNumber(phoneNumber)) {
                    is Resource.Success -> {
                        val facilitator = resource.data
                        facilitator?.let { loginUseCase.userLoggedIn(it.ID) }
                        LoginState.Success((facilitator?.toFacilitatorDataItem()))
                    }

                    is Resource.Error -> LoginState.Error(resource.error)
                }
        }
    }

    fun login(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        if (phoneNumber.isNotEmpty()) {
            runBlocking {
                viewModelScope.launch { loginIntent.send(LoginIntent.FetchFacilitator) }
            }
        } else {
            Toast.makeText(
                application.applicationContext,
                application.getString(R.string.please_enter_require_data), Toast.LENGTH_SHORT
            ).show()
        }
    }
}