package com.cultivaet.hassad.core.util

import com.cultivaet.hassad.BuildConfig

object Constants {
    object Headers {
        const val X_AUTH_TOKEN = "X-Auth-Token"
    }

    object EndPoints {
        const val GetFacilitatorByPhoneNumber = "api/facilitator/search/{phone}"
    }

    object Database {
        const val NAME = "${BuildConfig.APPLICATION_ID}.db"
    }

    object Day {
        const val YESTERDAY = "Yesterday"
        const val TODAY = "Today"
        const val TOMORROW = "Tomorrow"
    }
}