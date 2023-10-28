package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Answer
import com.cultivaet.hassad.domain.model.remote.responses.Comment
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID

interface ContentRepository {
    suspend fun getAllCommentsByFacilitatorId(id: Int): Resource<List<Comment>>

    suspend fun getAnswerById(id: Int): Resource<Answer>

    suspend fun getFileByUUID(uuid: String): Resource<FileByUUID>
}