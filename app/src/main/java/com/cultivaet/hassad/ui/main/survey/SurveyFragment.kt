package com.cultivaet.hassad.ui.main.survey

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.databinding.FragmentSurveyBinding
import com.cultivaet.hassad.ui.main.farmers.FarmersBottomSheet
import com.cultivaet.hassad.ui.main.survey.intent.SurveyIntent
import com.cultivaet.hassad.ui.main.survey.viewstate.SurveyState
import com.google.android.material.textfield.TextInputLayout
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

    private var selectedFarmerId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSurveyBinding.inflate(inflater, container, false)

            observeViewModel()

            binding.rootContainer.addView(addTextInputLayout(getString(R.string.village)))

            binding.rootContainer.addView(
                addTextInputLayout(
                    getString(R.string.center),
                    isNumberType = true
                )
            )

            binding.rootContainer.addView(
                addTextInputLayout(
                    getString(R.string.typeOfWork), list = listOf(
                        getString(R.string.typeOfWorkFirstOption),
                        getString(R.string.typeOfWorkSecondOption)
                    )
                )
            )

            runBlocking {
                lifecycleScope.launch { surveyViewModel.surveyIntent.send(SurveyIntent.GetUserId) }
            }

            binding.listOfFarmers.setOnClickListener {
                val farmersBottomSheet = surveyViewModel.farmersList?.let { farmers ->
                    FarmersBottomSheet(
                        farmers,
                        isSelectedOption = true,
                        selectedFarmerId
                    ) { farmerId ->
                        if (farmerId != null && farmerId != -1) {
                            selectedFarmerId = farmerId
                            binding.scrollView.visibility = View.VISIBLE
                            binding.selectFarmerMsgTextView.visibility = View.GONE
                        }
                    }
                }
                farmersBottomSheet?.show(parentFragmentManager, "farmersBottomSheet")
            }
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

    private fun addTextInputLayout(
        hintText: String,
        isNumberType: Boolean = false,
        list: List<String> = listOf()
    ): TextInputLayout {
        val textInputLayout = LayoutInflater.from(requireContext())
            .inflate(
                if (list.isEmpty()) R.layout.text_input_layout_text else R.layout.text_input_layout_drop_down_list,
                null
            ) as TextInputLayout
        textInputLayout.hint = hintText
        if (list.isNotEmpty())
            textInputLayout.fillListOfTypesToAdapter(
                requireContext(), list
            )
        if (list.isEmpty() && isNumberType) textInputLayout.editText?.inputType =
            InputType.TYPE_CLASS_NUMBER
        return textInputLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        selectedFarmerId = -1
    }
}