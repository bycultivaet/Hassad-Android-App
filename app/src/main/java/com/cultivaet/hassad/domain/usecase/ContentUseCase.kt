package com.cultivaet.hassad.domain.usecase

import com.cultivaet.hassad.domain.repository.ContentRepository

class ContentUseCase(private val repository: ContentRepository) {
    suspend fun getAllCommentsByFacilitatorId(
        id: Int,
    ) = repository.getAllCommentsByFacilitatorId(id)
}