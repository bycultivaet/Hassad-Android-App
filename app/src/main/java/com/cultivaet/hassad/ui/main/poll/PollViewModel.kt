package com.cultivaet.hassad.ui.main.poll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PollViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "صفحة الاستبيان"
    }
    val text: LiveData<String> = _text
}