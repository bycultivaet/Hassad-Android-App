package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Comment
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID
import com.cultivaet.hassad.domain.model.remote.responses.Note
import retrofit2.Response

interface ContentRepository {
    suspend fun getAllCommentsByFacilitatorId(id: Int): Resource<List<Comment>>

    suspend fun getFileByUUID(uuid: String): Resource<FileByUUID>
}