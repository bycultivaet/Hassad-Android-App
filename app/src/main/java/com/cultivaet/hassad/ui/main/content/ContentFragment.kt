package com.cultivaet.hassad.ui.main.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.databinding.FragmentContentBinding
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.content.intent.ContentIntent
import com.cultivaet.hassad.ui.main.content.viewstate.ContentState
import com.cultivaet.hassad.ui.main.notes.NoteDataItem
import com.cultivaet.hassad.ui.main.notes.NotesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class ContentFragment : Fragment() {
    private val contentViewModel: ContentViewModel by inject()

    private var _binding: FragmentContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var commentsAdapter: NotesAdapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentContentBinding.inflate(inflater, container, false)

            observeViewModel()

            contentViewModel.userId = (activity as MainActivity).getUserId()

            binding.commentsRecyclerView.adapter = commentsAdapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { contentViewModel.contentIntent.send(ContentIntent.FetchAllComments) }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            contentViewModel.state.collect {
                when (it) {
                    is ContentState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is ContentState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ContentState.Success<*> -> {
                        binding.progressBar.visibility = View.GONE

                        when (it.data) {
                            is List<*> -> {
                                binding.noContent.visibility = View.GONE

                                commentsAdapter.setItems(it.data as List<NoteDataItem>)
                            }

                            else -> {}
                        }
                    }

                    is ContentState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}