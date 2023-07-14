package com.cultivaet.hassad.ui.main.missions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MissionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is missions Fragment"
    }
    val text: LiveData<String> = _text
}