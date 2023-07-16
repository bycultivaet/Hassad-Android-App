package com.cultivaet.hassad.domain.repository

import kotlinx.coroutines.flow.Flow

interface SplashRepository {
    suspend fun isLoggedIn(): Flow<Boolean?>
}