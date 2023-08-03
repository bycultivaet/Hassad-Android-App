package com.cultivaet.hassad.ui.main.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.databinding.FragmentSurveyBinding
import com.cultivaet.hassad.ui.main.farmers.FarmersBottomSheet
import com.cultivaet.hassad.ui.main.survey.intent.SurveyIntent
import com.cultivaet.hassad.ui.main.survey.viewstate.SurveyState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class SurveyFragment : Fragment() {
    private val surveyViewModel: SurveyViewModel by inject()

    private var _binding: FragmentSurveyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)

        observeViewModel()

        runBlocking {
            lifecycleScope.launch { surveyViewModel.surveyIntent.send(SurveyIntent.GetUserId) }
        }

        binding.listOfFarmers.setOnClickListener {
            val farmersBottomSheet = surveyViewModel.farmersList?.let { farmers ->
                FarmersBottomSheet(
                    farmers
                )
            }
            farmersBottomSheet?.show(parentFragmentManager, "farmersBottomSheet")
        }

        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            surveyViewModel.state.collect {
                when (it) {
                    is SurveyState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.numberOfFarmersTextView.text =
                            getString(R.string.numberOfFarmersInList, 0)
                    }

                    is SurveyState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is SurveyState.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        if (it.data is List<*>)
                            binding.numberOfFarmersTextView.text =
                                getString(R.string.numberOfFarmersInList, it.data.size)
                        else {
                            Toast.makeText(
                                activity,
                                getString(R.string.added_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    is SurveyState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}