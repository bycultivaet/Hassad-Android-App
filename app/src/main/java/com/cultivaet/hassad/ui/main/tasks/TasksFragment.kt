package com.cultivaet.hassad.ui.main.tasks

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.getEncoded64ImageStringFromBitmap
import com.cultivaet.hassad.databinding.FragmentTasksBinding


class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val REQUEST_CODE = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        textView.text = getString(R.string.no_missions)

        textView.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            binding.imageTest.setImageBitmap(bitmap)

            Log.d("TAG", "onActivityResult: ${bitmap.getEncoded64ImageStringFromBitmap()}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}