package com.cultivaet.hassad.ui.main

import android.location.Address

interface AddressListener {
    fun onAddressChanged(address: Address?)
}