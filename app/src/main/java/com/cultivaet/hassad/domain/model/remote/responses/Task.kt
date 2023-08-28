package com.cultivaet.hassad.domain.model.remote.responses

import com.cultivaet.hassad.core.extension.getDateFromAPI
import com.cultivaet.hassad.ui.main.tasks.TaskDataItem

data class Task(
    val ID: Int,
    val title: String,
    val text: String,
    val from: String,
    val to: String,
    val status: Boolean
) {
    fun toTaskDataItem() = TaskDataItem(
        id = ID,
        title = title,
        text = text,
        from = from.getDateFromAPI(),
        to = to.getDateFromAPI(),
        isChecked = status,
    )
}