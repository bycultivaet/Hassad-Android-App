package com.cultivaet.hassad.domain.model.remote.responses

import com.cultivaet.hassad.ui.main.content.CommentDataItem

data class Comment(
    val answer_id: Int,
    val consultant_id: Int,
    val farmer_first_name: String,
    val farmer_id: Int,
    val farmer_last_name: String,
    val media_type: String,
    val media_uuid: String,
    val text: String
) {
    fun toCommentDataItem() = CommentDataItem(
        answerId = answer_id,
        consultantId = consultant_id,
        farmerFirstName = farmer_first_name,
        farmerId = farmer_id,
        farmerLastName = farmer_last_name,
        mediaType = media_type,
        mediaUuid = media_uuid,
        text = text
    )
}