package com.cultivaet.hassad.ui.main.content

data class CommentDataItem(
    val answerId: Int,
    val consultantId: Int,
    val farmerFirstName: String,
    val farmerId: Int,
    val farmerLastName: String,
    val mediaType: String,
    val mediaUuid: String,
    val text: String,
    var isExpandable: Boolean = false,
    var answerText: String? = "",
    var base64: String? = null,
)