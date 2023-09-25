package com.cultivaet.hassad.ui.media

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cultivaet.hassad.core.util.MediaType
import com.cultivaet.hassad.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val mediaType = bundle?.getString("mediaType")
        val decodedString = bundle?.getByteArray("base64")

        if (mediaType == MediaType.IMAGE.toString()) {
            val decodedImage = decodedString?.let { BitmapFactory.decodeByteArray(decodedString, 0, it.size) }
            binding.photoView.setImageBitmap(decodedImage)
        } else {
            binding.pdfView.fromBytes(decodedString).load()
        }
    }
}