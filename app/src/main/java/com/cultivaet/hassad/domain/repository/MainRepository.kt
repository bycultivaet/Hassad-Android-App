package com.cultivaet.hassad.domain.repository

interface MainRepository {
    suspend fun userLoggedOut()
}