package com.cultivaet.hassad.ui.main

import android.location.Location

interface FragmentRefreshListener {
    fun onLocationChanged(location: Location)
}