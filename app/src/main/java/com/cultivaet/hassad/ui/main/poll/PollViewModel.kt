package com.cultivaet.hassad.ui.main.poll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PollViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is poll Fragment"
    }
    val text: LiveData<String> = _text
}