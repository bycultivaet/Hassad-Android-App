package com.cultivaet.hassad.ui.main.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.databinding.FragmentTasksBinding
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.notes.NoteDataItem
import com.cultivaet.hassad.ui.main.notes.NotesAdapter
import com.cultivaet.hassad.ui.main.tasks.intent.TasksIntent
import com.cultivaet.hassad.ui.main.tasks.viewstate.TasksState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class TasksFragment : Fragment() {
    private val tasksViewModel: TasksViewModel by inject()

    private var _binding: FragmentTasksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tasksAdapter: TasksAdapter

    private val notesAdapter: NotesAdapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentTasksBinding.inflate(inflater, container, false)

            observeViewModel()

            tasksViewModel.userId = (activity as MainActivity).getUserId()

            tasksAdapter = TasksAdapter {
                tasksViewModel.taskDataItem = it
                runBlocking {
                    lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.UpdateTaskStatus) }
                }
            }
            binding.tasksRecyclerView.adapter = tasksAdapter

            binding.notesRecyclerView.adapter = notesAdapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.FetchAllTasks) }

            lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.FetchAllNotes) }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            tasksViewModel.state.collect {
                when (it) {
                    is TasksState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is TasksState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is TasksState.Success<*> -> {
                        binding.progressBar.visibility = View.GONE

                        when (val result = it.data) {
                            is List<*> -> {
                                if (result.isNotEmpty() && result[0] is TaskDataItem) {
                                    binding.listsLinearLayout.visibility = View.VISIBLE
                                    binding.noTasks.visibility = View.GONE

                                    tasksAdapter.setItems(result as List<TaskDataItem>)
                                } else if (result.isNotEmpty() && result[0] is NoteDataItem) {
                                    notesAdapter.setItems(result as List<NoteDataItem>)
                                }
                            }

                            else -> {}
                        }
                    }

                    is TasksState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}