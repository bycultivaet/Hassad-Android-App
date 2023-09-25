package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.core.source.remote.ApiService
import com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Comment
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import com.cultivaet.hassad.domain.model.remote.responses.Note
import com.cultivaet.hassad.domain.model.remote.responses.Task
import com.cultivaet.hassad.domain.model.remote.responses.UpdateStatus
import okhttp3.MultipartBody
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getFacilitator(
        phoneNumber: String
    ) = apiService.getFacilitatorByPhoneNumber(phoneNumber)

    override suspend fun getFacilitator(id: Int) = apiService.getFacilitatorById(id)
    override suspend fun getAllFarmersById(
        id: Int,
        filter: Boolean
    ): Response<List<Farmer>> = apiService.getAllFarmersById(id, filter)

    override suspend fun addFarmer(
        farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer
    ): Response<Farmer> = apiService.addFarmer(farmer)

    override suspend fun getFacilitatorForm(
        id: Int
    ): Response<Form> = apiService.getFacilitatorForm(id)

    override suspend fun submitFacilitatorAnswer(
        facilitatorAnswer: FacilitatorAnswer
    ): Response<com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer> =
        apiService.submitFacilitatorAnswer(facilitatorAnswer)

    override suspend fun uploadImage(
        image: MultipartBody.Part
    ): Response<ImageUUID> = apiService.uploadImage(image)

    override suspend fun getAllTasksById(
        id: Int
    ): Response<List<Task>> = apiService.getAllTasksById(id)

    override suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ): Response<UpdateStatus> = apiService.updateTaskStatus(facilitatorId, taskId, status)

    override suspend fun getAllNotesById(
        id: Int
    ): Response<List<Note>> = apiService.getAllNotesById(id)

    override suspend fun getAllCommentsByFacilitatorId(
        id: Int
    ): Response<List<Comment>> = apiService.getAllCommentsByFacilitatorId(id)

    override suspend fun getFileByUUID(
        uuid: String
    ): Response<FileByUUID> = apiService.getFileByUUID(uuid)
}