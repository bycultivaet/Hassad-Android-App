package com.cultivaet.hassad.ui.main.tasks

data class TaskDataItem(
    val id: Int,
    val title: String,
    val text: String,
    val from: String,
    val to: String,
    var isChecked: Boolean = false
)