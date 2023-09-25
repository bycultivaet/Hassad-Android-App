package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Comment
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID
import com.cultivaet.hassad.domain.model.remote.responses.Note
import com.cultivaet.hassad.domain.repository.ContentRepository

class ContentRepositoryImpl(
    application: Application,
    private val apiHelper: ApiHelper,
) : BaseRepository(application), ContentRepository {
    override suspend fun getAllCommentsByFacilitatorId(
        id: Int
    ): Resource<List<Comment>> = safeApiCall { apiHelper.getAllCommentsByFacilitatorId(id) }

    override suspend fun getFileByUUID(
        uuid: String
    ): Resource<FileByUUID> = safeApiCall { apiHelper.getFileByUUID(uuid) }
}