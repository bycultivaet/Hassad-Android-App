package com.cultivaet.hassad.core.extension

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.launchActivity(
    withFinish: Boolean = false
) {
    startActivity(Intent(this, T::class.java))
    if (withFinish) this.finish()
}