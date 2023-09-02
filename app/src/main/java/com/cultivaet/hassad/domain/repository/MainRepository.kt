package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import okhttp3.MultipartBody

interface MainRepository {
    suspend fun userLoggedOut()

    // -------------- Facilitator for caching --------------
    suspend fun submitFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
    ): Resource<FacilitatorAnswer>

    suspend fun deleteFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    )

    suspend fun getFacilitatorAnswers(): List<com.cultivaet.hassad.domain.model.local.FacilitatorAnswer>

    suspend fun uploadImage(
        image: MultipartBody.Part
    ): Resource<ImageUUID>

    // -------------- Farmer for caching --------------
    suspend fun submitAddFarmer(
        farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer
    ): Resource<Farmer>

    suspend fun deleteFarmer(
        farmer: com.cultivaet.hassad.domain.model.local.Farmer
    )

    suspend fun getFarmers(): List<com.cultivaet.hassad.domain.model.local.Farmer>

}