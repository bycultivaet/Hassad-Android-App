package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.MainRepository

class MainUseCase(private val repository: MainRepository) {
    suspend fun userLoggedOut() = repository.userLoggedOut()
}