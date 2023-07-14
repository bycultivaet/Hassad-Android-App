package com.cultivaet.hassad.ui.main.addition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdditionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is addition Fragment"
    }
    val text: LiveData<String> = _text
}