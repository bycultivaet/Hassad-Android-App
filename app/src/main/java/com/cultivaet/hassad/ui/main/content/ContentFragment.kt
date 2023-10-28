package com.cultivaet.hassad.ui.main.content

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.core.util.Constants
import com.cultivaet.hassad.core.util.Utils
import com.cultivaet.hassad.databinding.FragmentContentBinding
import com.cultivaet.hassad.domain.model.remote.responses.Answer
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
        CommentsAdapter(requireActivity(),
            viewMedia = { position ->
                contentViewModel.position = position
                val task = commentsAdapter.mList[position]
                if (Constants.cacheMedia.contains(task.mediaUuid)) {
                    activity?.launchActivity<MediaActivity>(Bundle().apply {
                        putString(
                            "mediaType",
                            Utils.getMediaType(task.mediaType).toString()
                        )
                        putString(
                            "mediaId",
                            task.mediaUuid
                        )
                    })
                } else {
                    // image -> ab819a3e-c788-47cc-a7e7-b63a9864db2f
                    // pdf -> 655982f7-4105-4bc3-b961-7932c8db39bc
                    contentViewModel.uuid = task.mediaUuid
                    runBlocking {
                        lifecycleScope.launch { contentViewModel.contentIntent.send(ContentIntent.FetchFile) }
                    }
                }
            }, answerTextById = { position ->
                contentViewModel.position = position
                val task = commentsAdapter.mList[position]
                contentViewModel.answerId = task.answerId
                if (task.answerText.isNullOrEmpty()) {
                    runBlocking {
                        lifecycleScope.launch { contentViewModel.contentIntent.send(ContentIntent.FetchAnswerById) }
                    }
                } else {
                    task.isExpandable = !task.isExpandable
                    commentsAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentContentBinding.inflate(inflater, container, false)

            observeViewModel()

            contentViewModel.userId = (activity as MainActivity).getUserId()

            binding.commentsRecyclerView.adapter = commentsAdapter

            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchStr = s.toString()
                    val filteredList = contentViewModel.commentsList?.filter {
                        it.farmerFirstName.contains(searchStr) || it.farmerLastName.contains(
                            searchStr
                        )
                    }
                    if (filteredList != null) {
                        commentsAdapter.setItems(filteredList)
                    }
                }
            })
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
                            is Answer -> {
                                commentsAdapter.mList[contentViewModel.position].answerText = it.data.toString()
                                commentsAdapter.mList[contentViewModel.position].isExpandable = true
                                commentsAdapter.notifyDataSetChanged()
                            }

                            is List<*> -> {
                                if (it.data.size > 0) {
                                    binding.noContent.visibility = View.GONE
                                    binding.searchEditText.visibility = View.VISIBLE
                                    binding.contentLinearLayout.visibility = View.VISIBLE

                                    commentsAdapter.setItems(it.data as MutableList<CommentDataItem>)
                                } else {
                                    binding.searchEditText.visibility = View.GONE
                                }
                            }

                            is FileByUUID -> {
                                Constants.cacheMedia[commentsAdapter.mList[contentViewModel.position].mediaUuid] =
                                    it.data.image
                                activity?.launchActivity<MediaActivity>(Bundle().apply {
                                    putString(
                                        "mediaType",
                                        Utils.getMediaType(
                                            commentsAdapter.mList[contentViewModel.position].mediaType
                                        ).toString()
                                    )
                                    putString(
                                        "mediaId",
                                        commentsAdapter.mList[contentViewModel.position].mediaUuid
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