package com.cultivaet.hassad.data.source.remote

import com.cultivaet.hassad.domain.model.remote.responses.Answer
import com.cultivaet.hassad.domain.model.remote.responses.Comment
import com.cultivaet.hassad.domain.model.remote.responses.Facilitator
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
import com.cultivaet.hassad.domain.model.remote.responses.Note
import com.cultivaet.hassad.domain.model.remote.responses.Task
import com.cultivaet.hassad.domain.model.remote.responses.UpdateStatus
import okhttp3.MultipartBody
import retrofit2.Response

interface ApiHelper {
    suspend fun getFacilitator(phoneNumber: String): Response<Facilitator>

    suspend fun getFacilitator(id: Int): Response<Facilitator>

    suspend fun getAllFarmersById(id: Int, filter: Boolean): Response<List<Farmer>>

    suspend fun addFarmer(
        farmer: com.cultivaet.hassad.domain.model.remote.requests.Farmer
    ): Response<Farmer>

    suspend fun getFacilitatorForm(id: Int): Response<Form>

    suspend fun submitFacilitatorAnswer(
        facilitatorAnswer: com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
    ): Response<FacilitatorAnswer>

    suspend fun uploadImage(image: MultipartBody.Part): Response<ImageUUID>

    suspend fun getAllTasksById(id: Int): Response<List<Task>>

    suspend fun updateTaskStatus(
        facilitatorId: Int,
        taskId: Int,
        status: Boolean
    ): Response<UpdateStatus>

    suspend fun getAllNotesById(id: Int): Response<List<Note>>

    suspend fun getAllCommentsByFacilitatorId(id: Int): Response<List<Comment>>

    suspend fun getAnswerById(id: Int): Response<Answer>

    suspend fun getFileByUUID(uuid: String): Response<FileByUUID>
}