package com.cultivaet.hassad.core.util

import android.graphics.Bitmap
import com.google.gson.Gson

object Utils {
    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    fun <T> toJson(obj: T): String {
        return Gson().toJson(obj)
    }

    fun dummyBitmap(): Bitmap = Bitmap.createBitmap(
        1,
        1,
        Bitmap.Config.RGB_565
    )
}