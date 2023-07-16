package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.SplashRepository

class SplashUseCase(private val repository: SplashRepository) {
    suspend fun isLoggedIn() = repository.isLoggedIn()
}