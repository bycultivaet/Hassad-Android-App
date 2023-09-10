package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.ContentRepository

class ContentUseCase(private val repository: ContentRepository) {
    suspend fun getAllCommentsByFormId(
        formId: Int,
    ) = repository.getAllCommentsByFormId(formId)
}