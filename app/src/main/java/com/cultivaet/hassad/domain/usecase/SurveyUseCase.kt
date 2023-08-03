package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.SurveyRepository

class SurveyUseCase(private val repository: SurveyRepository) {
    suspend fun userId() = repository.userId()

    suspend fun getAllFarmersById(id: Int) = repository.getAllFarmersById(id)
}