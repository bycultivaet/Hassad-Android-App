package com.cultivaet.hassad.core.util

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.Window
import android.widget.ImageView
import com.cultivaet.hassad.R

class ImagePopupDialog(context: Context, private val imageBase64: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.full_image_dialog)

        val fullImageView = findViewById<ImageView>(R.id.fullImageView)

        val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        fullImageView.setImageBitmap(decodedImage)

        fullImageView.setOnClickListener { dismiss() }
    }
}