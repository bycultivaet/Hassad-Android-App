package com.cultivaet.hassad.domain.repository

import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.responses.Note

interface ContentRepository {
    suspend fun getAllCommentsByFormId(formId: Int): Resource<List<Note>>
}