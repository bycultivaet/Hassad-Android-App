package com.cultivaet.hassad.data.repository

import android.app.Application
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.domain.model.remote.responses.Note
import com.cultivaet.hassad.domain.repository.ContentRepository

class ContentRepositoryImpl(
    application: Application,
    private val apiHelper: ApiHelper,
) : BaseRepository(application), ContentRepository {
    override suspend fun getAllCommentsByFormId(
        formId: Int
    ): Resource<List<Note>> = safeApiCall { apiHelper.getAllCommentsByFormId(formId) }
}