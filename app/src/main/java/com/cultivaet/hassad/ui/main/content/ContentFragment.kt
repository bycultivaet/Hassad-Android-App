package com.cultivaet.hassad.ui.main.content

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.core.util.ImagePopupDialog
import com.cultivaet.hassad.core.util.Utils
import com.cultivaet.hassad.databinding.FragmentContentBinding
import com.cultivaet.hassad.domain.model.remote.responses.FileByUUID
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.content.intent.ContentIntent
import com.cultivaet.hassad.ui.main.content.viewstate.ContentState
import com.cultivaet.hassad.ui.media.MediaActivity
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

    private val commentsAdapter: CommentsAdapter by lazy {
        CommentsAdapter { position ->
            contentViewModel.position = position
            val task = commentsAdapter.mList[position]
            if (task.base64 != null) {
                activity?.launchActivity<MediaActivity>(Bundle().apply {
                    putString(
                        "mediaType",
                        Utils.getMediaType(task.mediaType).toString()
                    )
                    putByteArray("base64", Base64.decode(task.base64, Base64.DEFAULT))
                })
            } else {
                // image -> ab819a3e-c788-47cc-a7e7-b63a9864db2f
                // pdf -> 655982f7-4105-4bc3-b961-7932c8db39bc
//                contentViewModel.uuid = task.mediaUuid
                contentViewModel.uuid = "ab819a3e-c788-47cc-a7e7-b63a9864db2f"
                runBlocking {
                    lifecycleScope.launch { contentViewModel.contentIntent.send(ContentIntent.FetchFile) }
                }
            }
        }
    }

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

                                commentsAdapter.setItems(it.data as MutableList<CommentDataItem>)
                            }

                            is FileByUUID -> {
                                commentsAdapter.mList[contentViewModel.position].base64 =
                                    it.data.image
//                                ImagePopupDialog(requireContext(), it.data.image).apply { show() }
                                activity?.launchActivity<MediaActivity>(Bundle().apply {
                                    putString(
                                        "mediaType",
                                        Utils.getMediaType(commentsAdapter.mList[contentViewModel.position].mediaType)
                                            .toString()
                                    )
                                    putByteArray(
                                        "base64",
                                        Base64.decode(it.data.image, Base64.DEFAULT)
                                    )
                                })
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