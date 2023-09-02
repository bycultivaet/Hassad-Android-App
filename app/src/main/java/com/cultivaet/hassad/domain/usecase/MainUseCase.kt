package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import com.cultivaet.hassad.domain.repository.MainRepository
import okhttp3.MultipartBody

class MainUseCase(private val repository: MainRepository) {
    suspend fun userLoggedOut() = repository.userLoggedOut()

    // -------------- Facilitator for caching --------------
    suspend fun submitFacilitatorAnswer(facilitatorAnswer: com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer): Resource<FacilitatorAnswer> =
        repository.submitFacilitatorAnswer(facilitatorAnswer)

    suspend fun deleteFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    ) = repository.deleteFacilitatorAnswer(facilitatorAnswer)

    suspend fun getFacilitatorAnswers(): List<com.cultivaet.hassad.domain.model.local.FacilitatorAnswer> =
        repository.getFacilitatorAnswers()

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): Resource<ImageUUID> = repository.uploadImage(image)

    // -------------- Farmer for caching --------------
    suspend fun submitAddFarmer(
        farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer
    ) = repository.submitAddFarmer(farmer)

    suspend fun deleteFarmer(
        farmer: com.cultivaet.hassad.domain.model.local.Farmer
    ) = repository.deleteFarmer(farmer)

    suspend fun getFarmers(): List<com.cultivaet.hassad.domain.model.local.Farmer> =
        repository.getFarmers()
}