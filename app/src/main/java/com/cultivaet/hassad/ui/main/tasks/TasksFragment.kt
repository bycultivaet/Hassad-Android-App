package com.cultivaet.hassad.ui.main.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cultivaet.hassad.R
import com.cultivaet.hassad.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}