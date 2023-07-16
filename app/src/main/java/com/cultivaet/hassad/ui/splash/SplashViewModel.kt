package com.cultivaet.hassad.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.domain.usecase.SplashUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class SplashViewModel(
    private val splashUseCase: SplashUseCase
) : ViewModel() {
    fun handleLoggedInState(launchActivity: (isLoggedIn: Boolean?) -> Unit) {
        runBlocking {
            viewModelScope.launch {
                splashUseCase.isLoggedIn().collect { isLoggedIn ->
                    delay(3000)
                    launchActivity(isLoggedIn)
                }
            }
        }
    }
}