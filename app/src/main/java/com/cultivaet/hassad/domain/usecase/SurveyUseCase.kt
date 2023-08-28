package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import com.cultivaet.hassad.domain.repository.SurveyRepository
import okhttp3.MultipartBody

class SurveyUseCase(private val repository: SurveyRepository) {
    suspend fun userId() = repository.userId()

    suspend fun getAllFarmersById(
        id: Int,
        filter: Boolean = true
    ) = repository.getAllFarmersById(id, filter)

    suspend fun getFacilitatorForm(id: Int) = repository.getFacilitatorForm(id)

    suspend fun submitFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
    ): Resource<FacilitatorAnswer> = repository.submitFacilitatorAnswer(facilitatorAnswer)

    suspend fun insertFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    ) = repository.insertFacilitatorAnswer(facilitatorAnswer)

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): Resource<ImageUUID> = repository.uploadImage(image)
}