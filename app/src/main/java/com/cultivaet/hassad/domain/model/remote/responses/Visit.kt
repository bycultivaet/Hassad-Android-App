package com.cultivaet.hassad.domain.model.remote.responses

import com.cultivaet.hassad.ui.main.tasks.visits.VisitDataItem

data class Visit(
    val ID: Int,
    val facilitator_id: Int,
    val form_id: Int,
    val from: String,
    val group_id: Int,
    val name: String,
    val status: String,
    val to: String,
    val visit_type: String
) {
    fun toVisitDataItem() = VisitDataItem(
        id = ID,
        title = name,
        visitType = visit_type,
        from = from,
        to = to,
    )
}