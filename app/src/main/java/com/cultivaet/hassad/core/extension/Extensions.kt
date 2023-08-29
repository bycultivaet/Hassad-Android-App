package com.cultivaet.hassad.core.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import com.cultivaet.hassad.R
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

inline fun <reified T : Activity> Activity.launchActivity(
    withFinish: Boolean = false
) {
    startActivity(Intent(this, T::class.java))
    overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom)
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

fun Long.getDateFromString(): String {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utc.timeInMillis = this
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return format.format(utc.time)
}

fun String.getDateFromAPI(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    try {
        return formatter.format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(this))
    } catch (_: Exception) {
    }
    return ""
}

fun TextInputLayout.showError(context: Context): Boolean {
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
                textInputLayout.error = context.getString(R.string.please_enter)
                textInputLayout.requestFocus()
            } else {
                textInputLayout.error = null
            }
        }
    })

    val isEmpty = textInputLayout.editText?.text.toString().isEmpty()
    if (isEmpty) {
        textInputLayout.error = context.getString(R.string.please_enter)
        textInputLayout.requestFocus()
    } else {
        textInputLayout.error = null
    }

    return !isEmpty
}

fun TextInputLayout.fillListOfTypesToAdapter(context: Context, list: List<String>) {
    val arrayAdapter = ArrayAdapter(context, R.layout.list_item, list)
    (this.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
}

fun View.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    val layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParams.setMargins(left, top, right, bottom)
    this.layoutParams = layoutParams
}

fun Bitmap.getEncoded64ImageStringFromBitmap(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}