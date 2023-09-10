package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.local.DatabaseHelper
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import com.cultivaet.hassad.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

class MainRepositoryImpl(
    application: Application,
    private val preferencesDataSource: PreferencesDataSource,
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper
) : BaseRepository(application), MainRepository {
    override suspend fun userId(): Flow<Int?> = preferencesDataSource.userId()

    override suspend fun userLoggedOut() = preferencesDataSource.userLoggedOut()
    override suspend fun submitFacilitatorAnswer(
        facilitatorAnswer: FacilitatorAnswer
    ): Resource<com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer> =
        safeApiCall { apiHelper.submitFacilitatorAnswer(facilitatorAnswer) }

    override suspend fun deleteFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer
    ) = databaseHelper.deleteFacilitatorAnswer(facilitatorAnswer)

    override suspend fun getFacilitatorAnswers(): List<com.cultivaet.hassad.domain.model.local.FacilitatorAnswer> =
        databaseHelper.getFacilitatorAnswers()

    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): Resource<ImageUUID> = safeApiCall { apiHelper.uploadImage(image) }

    override suspend fun submitAddFarmer(
        farmer: Farmer
    ): Resource<com.cultivaet.hassad.domain.model.remote.responses.Farmer> =
        safeApiCall { apiHelper.addFarmer(farmer) }

    override suspend fun deleteFarmer(
        farmer: com.cultivaet.hassad.domain.model.local.Farmer
    ) = databaseHelper.deleteFarmer(farmer)

    override suspend fun getFarmers(): List<com.cultivaet.hassad.domain.model.local.Farmer> =
        databaseHelper.getFarmers()
}