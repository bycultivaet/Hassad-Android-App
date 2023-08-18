package com.cultivaet.hassad.core.source.remote

import com.cultivaet.hassad.core.util.Constants
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(Constants.EndPoints.GetFacilitatorByPhoneNumber)
    suspend fun getFacilitatorByPhoneNumber(@Path("phone") phoneNumber: String): Response<Facilitator>

    @GET(Constants.EndPoints.GetFacilitatorById)
    suspend fun getFacilitatorById(@Path("id") id: Int): Response<Facilitator>

    @GET(Constants.EndPoints.GetAllFarmersById)
    suspend fun getAllFarmersById(
        @Path("id") id: Int,
        @Query("filter") filter: Boolean
    ): Response<List<Farmer>>

    @POST(Constants.EndPoints.PostFarmer)
    suspend fun addFarmer(@Body farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer): Response<Farmer>

    @GET(Constants.EndPoints.GetFacilitatorForm)
    suspend fun getFacilitatorForm(@Path("id") id: Int): Response<Form>

    @POST(Constants.EndPoints.PostSubmitFacilitatorAnswer)
    suspend fun submitFacilitatorAnswer(@Body facilitatorAnswer: com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer): Response<FacilitatorAnswer>

    @Multipart
    @POST(Constants.EndPoints.PostImage)
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<ImageUUID>
}