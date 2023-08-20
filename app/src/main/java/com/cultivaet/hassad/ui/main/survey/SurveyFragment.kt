package com.cultivaet.hassad.ui.main.survey

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.core.extension.setMargin
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.FragmentSurveyBinding
import com.cultivaet.hassad.domain.model.remote.requests.Answer
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Field
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.farmers.bottomsheet.FarmersBottomSheet
import com.cultivaet.hassad.ui.main.survey.intent.SurveyIntent
import com.cultivaet.hassad.ui.main.survey.viewstate.SurveyState
import com.google.android.material.datepicker.MaterialDatePicker
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

    private var isNotEmptyWholeValidation = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSurveyBinding.inflate(inflater, container, false)

            observeViewModel()

            binding.listOfFarmers.setOnClickListener {
                val farmersBottomSheet = surveyViewModel.farmersList?.let { farmers ->
                    FarmersBottomSheet(
                        farmers,
                        isSelectedOption = true,
                        surveyViewModel.facilitatorAnswer.farmerId
                    ) { farmerId ->
                        if (farmerId != null && farmerId != -1) {
                            surveyViewModel.facilitatorAnswer.farmerId = farmerId

                            binding.rootContainer.removeAllViews()

                            (activity as MainActivity).getCurrentLocation()

                            runBlocking {
                                lifecycleScope.launch {
                                    surveyViewModel.surveyIntent.send(
                                        SurveyIntent.FetchFacilitatorForm
                                    )
                                }
                            }
                        }
                    }
                }
                farmersBottomSheet?.show(parentFragmentManager, "farmersBottomSheet")
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { surveyViewModel.surveyIntent.send(SurveyIntent.GetUserId) }
        }
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
                        when (it.data) {
                            is List<*> -> {
                                binding.numberOfFarmersTextView.text =
                                    getString(R.string.numberOfFarmersInList, it.data.size)
                            }

                            is Form -> {
                                renderDynamicViews(it.data)
                                binding.scrollView.visibility = View.VISIBLE
                                binding.selectFarmerMsgTextView.visibility = View.GONE
                            }

                            is FacilitatorAnswer -> {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.added_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()

                                binding.scrollView.visibility = View.GONE
                                binding.selectFarmerMsgTextView.visibility = View.VISIBLE

                                runBlocking {
                                    lifecycleScope.launch {
                                        surveyViewModel.surveyIntent.send(
                                            SurveyIntent.FetchAllFarmers
                                        )
                                    }
                                }
                            }

                            else -> {}
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

    private fun renderDynamicViews(form: Form) {
        surveyViewModel.facilitatorAnswer.formId = form.ID
        form.fields.forEachIndexed { index, field ->
            val viewGroup = when (field.type) {
                "date" -> {
                    addConstraintLayout(field)
                }

                else -> {
                    addTextInputLayout(field)
                }
            }
            surveyViewModel.facilitatorAnswer.answers.add(Answer((field.type)))

            if (index % 2 == 1) {
                val marginValue = resources.getDimension(com.intuit.sdp.R.dimen._4sdp).toInt()
                viewGroup.setMargin(top = marginValue, bottom = marginValue)
            }

            binding.rootContainer.addView(viewGroup)
        }

        binding.rootContainer.addView(addButton().apply {
            val marginValue = resources.getDimension(com.intuit.sdp.R.dimen._16sdp).toInt()
            setMargin(bottom = marginValue)
        })
    }

    private fun addTextInputLayout(field: Field): TextInputLayout {
        val textInputLayout = LayoutInflater.from(requireContext())
            .inflate(
                when (field.type) {
                    "select" -> {
                        R.layout.text_input_layout_drop_down_list
                    }

                    else -> {
                        R.layout.text_input_layout_text
                    }
                },
                null
            ) as TextInputLayout

        textInputLayout.hint = field.name

        textInputLayout.editText?.hint = field.placeholder

        textInputLayout.tag = field.name

        if (field.type == "select") {
            textInputLayout.fillListOfTypesToAdapter(
                requireContext(), field.options.map { it.label }
            )
        } else if (field.type == "number") {
            textInputLayout.editText?.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        return textInputLayout
    }

    private fun addConstraintLayout(field: Field): ConstraintLayout {
        val constraintLayout = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.text_input_layout_date,
                null
            ) as ConstraintLayout

        constraintLayout.tag = field.name

        val textInputLayout = constraintLayout.findViewById<TextInputLayout>(R.id.textInputLayout)

        textInputLayout.hint = field.name

        textInputLayout.editText?.hint = field.placeholder

        textInputLayout.tag = "textInputLayout.${field.name}"

        constraintLayout.findViewById<View>(R.id.view).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

            datePicker.apply {
                show(
                    this@SurveyFragment.requireActivity().supportFragmentManager,
                    "DATE_PICKER"
                )
                addOnPositiveButtonClickListener {
                    textInputLayout.editText?.setText(this.headerText)
                }
            }
        }
        return constraintLayout
    }

    private fun addButton(): Button {
        val button = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.button_item,
                null
            ) as Button

        button.setOnClickListener {
            isNotEmptyWholeValidation = true

            surveyViewModel.insertFacilitatorAnswer()

            val viewParent = it.parent
            if (viewParent is LinearLayout) {
                val count: Int = viewParent.childCount
                var answerIndex = 0
                for (index in 0 until count) {
                    val textInputLayout = when (val view: View = viewParent.getChildAt(index)) {
                        is TextInputLayout -> {
                            viewParent.findViewWithTag(view.tag)
                        }

                        is ConstraintLayout -> {
                            view.findViewWithTag<TextInputLayout>("textInputLayout.${view.tag}")
                        }

                        else -> {
                            null
                        }
                    }

                    if (textInputLayout != null) {
                        val isNotEmpty = textInputLayout.showError(requireActivity())
                        isNotEmptyWholeValidation = isNotEmptyWholeValidation && isNotEmpty

                        surveyViewModel.facilitatorAnswer.answers[answerIndex++].apply {
                            this.body = textInputLayout.editText?.text.toString()
                        }
                    }
                }

                val location = (activity as MainActivity).getLocation()
                Log.d("SurveyFragment", "locationAAA: $location")

                if (location != null) {
                    surveyViewModel.facilitatorAnswer.geolocation =
                        "${location.latitude}, ${location.longitude}"

                    Log.d(
                        "SurveyFragment",
                        "facilitatorAnswer: ${surveyViewModel.facilitatorAnswer}"
                    )

                    if (isNotEmptyWholeValidation) {
                        runBlocking {
                            lifecycleScope.launch {
                                surveyViewModel.surveyIntent.send(
                                    SurveyIntent.SubmitFacilitatorAnswer
                                )
                            }
                        }
                    }
                } else {
                    (activity as MainActivity).getCurrentLocation()
                }
            }
        }
        return button
    }
}