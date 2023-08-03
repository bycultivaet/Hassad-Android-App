package com.cultivaet.hassad.core.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cultivaet.hassad.R
import com.google.android.material.textfield.TextInputLayout
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

fun TextInputLayout.showError(context: Context, hint: String): Boolean {
    val textInputLayout = this
    textInputLayout.editText?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            val isEmpty = text.isEmpty()
            if (isEmpty) {
                textInputLayout.error = "${context.getString(R.string.please_enter)} $hint"
                textInputLayout.requestFocus()
            } else {
                textInputLayout.error = null
            }
        }
    })

    val isEmpty = textInputLayout.editText?.text.toString().isEmpty()
    if (isEmpty) {
        textInputLayout.error = "${context.getString(R.string.please_enter)} $hint"
        textInputLayout.requestFocus()
    } else {
        textInputLayout.error = null
    }

    return !isEmpty
}