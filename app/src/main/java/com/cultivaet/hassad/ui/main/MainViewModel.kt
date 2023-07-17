package com.cultivaet.hassad.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.domain.usecase.MainUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class MainViewModel(
    private val mainUseCase: MainUseCase
) : ViewModel() {
    fun loggedInState(goToLogin: () -> Unit) {
        runBlocking {
            viewModelScope.launch {
                mainUseCase.userLoggedOut()
                goToLogin()
            }
        }
    }
}