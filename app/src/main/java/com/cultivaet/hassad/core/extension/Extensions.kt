package com.cultivaet.hassad.core.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import com.cultivaet.hassad.R
import java.text.SimpleDateFormat
import java.util.Locale

inline fun <reified T : Activity> Activity.launchActivity(
    withFinish: Boolean = false
) {
    startActivity(Intent(this, T::class.java))
    if (withFinish) this.finish()
}

fun Context.logoutAlert(yesCallback: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.logout_message))
    builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
        yesCallback.invoke()
    }
    builder.setNegativeButton(getString(R.string.no)) { _, _ ->
    }
    val dialog = builder.create()
    dialog.window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL
    dialog.show()
}

fun String.getDateFromString(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    return formatter.format(SimpleDateFormat("d MMM yyyy").parse(this))
}