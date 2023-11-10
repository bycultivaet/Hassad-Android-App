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
import com.cultivaet.hassad.ui.main.tasks.intent.TasksIntent
import com.cultivaet.hassad.ui.main.tasks.notes.NoteDataItem
import com.cultivaet.hassad.ui.main.tasks.notes.NotesAdapter
import com.cultivaet.hassad.ui.main.tasks.viewstate.TasksState
import com.cultivaet.hassad.ui.main.tasks.visits.VisitDataItem
import com.cultivaet.hassad.ui.main.tasks.visits.VisitsAdapter
import com.google.android.material.tabs.TabLayout
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

    private val schedulesAdapter: VisitsAdapter = VisitsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentTasksBinding.inflate(inflater, container, false)

            observeViewModel()

            setupTabLayout()

            tasksViewModel.userId = (activity as MainActivity).getUserId()

            tasksAdapter = TasksAdapter {
                tasksViewModel.taskDataItem = it
                runBlocking {
                    lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.UpdateTaskStatus) }
                }
            }
            binding.tasksRecyclerView.adapter = tasksAdapter

            binding.notesRecyclerView.adapter = notesAdapter

            binding.schedulesRecyclerView.adapter = schedulesAdapter
        }
        return binding.root
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.tasks.visibility = View.VISIBLE
                    binding.notes.visibility = View.GONE
                    binding.schedules.visibility = View.GONE
                } else if (tab?.position == 1) {
                    binding.tasks.visibility = View.GONE
                    binding.notes.visibility = View.VISIBLE
                    binding.schedules.visibility = View.GONE
                } else if (tab?.position == 2) {
                    binding.tasks.visibility = View.GONE
                    binding.notes.visibility = View.GONE
                    binding.schedules.visibility = View.VISIBLE
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.FetchAllTasks) }

            lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.FetchAllNotes) }

            lifecycleScope.launch { tasksViewModel.tasksIntent.send(TasksIntent.FetchAllVisits) }
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
                                    binding.noTasks.visibility = View.GONE

                                    tasksAdapter.setItems(result as List<TaskDataItem>)
                                } else if (result.isNotEmpty() && result[0] is NoteDataItem) {
                                    binding.noNotes.visibility = View.GONE

                                    notesAdapter.setItems(result as List<NoteDataItem>)
                                } else if (result.isNotEmpty() && result[0] is VisitDataItem) {
                                    binding.noSchedules.visibility = View.GONE

                                    schedulesAdapter.mList.addAll(result as List<VisitDataItem>)
                                    schedulesAdapter.setItems(schedulesAdapter.mList)
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