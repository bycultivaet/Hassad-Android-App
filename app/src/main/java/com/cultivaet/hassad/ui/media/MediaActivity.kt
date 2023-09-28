package com.cultivaet.hassad.ui.media

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.cultivaet.hassad.core.util.Constants
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
        val mediaId = bundle?.getString("mediaId")

        val byteArray = Base64.decode(Constants.cacheMedia[mediaId], Base64.DEFAULT)

        if (mediaType == MediaType.IMAGE.toString()) {
            val decodedImage = byteArray?.let {
                BitmapFactory.decodeByteArray(byteArray, 0, it.size)
            }
            binding.photoView.setImageBitmap(decodedImage)
        } else {
            binding.pdfView.fromBytes(byteArray).load()
        }

        binding.close.setOnClickListener { finish() }
    }
}