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
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.ui.main.farmers.FarmersBottomSheet
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSurveyBinding.inflate(inflater, container, false)

            observeViewModel()

            runBlocking {
                lifecycleScope.launch { surveyViewModel.surveyIntent.send(SurveyIntent.GetUserId) }
            }

            binding.listOfFarmers.setOnClickListener {
                val farmersBottomSheet = surveyViewModel.farmersList?.let { farmers ->
                    FarmersBottomSheet(
                        farmers,
                        isSelectedOption = true,
                        surveyViewModel.farmerId
                    ) { farmerId ->
                        if (farmerId != null && farmerId != -1) {
                            surveyViewModel.farmerId = farmerId
                            runBlocking {
                                lifecycleScope.launch {
                                    surveyViewModel.surveyIntent.send(
                                        SurveyIntent.FetchFarmerForm
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

                            else -> {
                                Toast.makeText(
                                    activity,
                                    getString(R.string.added_successfully),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
        form.fields.forEachIndexed { index, field ->
            val viewGroup = when (field.type) {
                "select" -> {
                    addTextInputLayout(
                        field.name, field.placeholder,
                        field.type,
                        list = field.options.map { it.label })
                }

                "number" -> {
                    addTextInputLayout(field.name, field.placeholder, field.type)
                }

                "date" -> {
                    addConstraintLayout(field.name, field.placeholder)
                }

                else -> {
                    addTextInputLayout(field.name, field.placeholder, field.type)
                }
            }
            surveyViewModel.answers.add(Answer((field.type)))

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

    private fun addTextInputLayout(
        name: String,
        placeholder: String,
        type: String,
        list: List<String> = listOf()
    ): TextInputLayout {
        val textInputLayout = LayoutInflater.from(requireContext())
            .inflate(
                when (type) {
                    "select" -> {
                        R.layout.text_input_layout_drop_down_list
                    }

                    else -> {
                        R.layout.text_input_layout_text
                    }
                },
                null
            ) as TextInputLayout

        textInputLayout.hint = placeholder
        textInputLayout.tag = name

        if (type == "select") {
            textInputLayout.fillListOfTypesToAdapter(
                requireContext(), list
            )
        } else if (type == "number") {
            textInputLayout.editText?.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        return textInputLayout
    }

    private fun addConstraintLayout(
        name: String,
        placeholder: String,
    ): ConstraintLayout {
        val constraintLayout = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.text_input_layout_date,
                null
            ) as ConstraintLayout

        constraintLayout.tag = name

        val textInputLayout = constraintLayout.findViewById<TextInputLayout>(R.id.textInputLayout)

        textInputLayout.hint = placeholder

        textInputLayout.tag = "textInputLayout.$name"

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
            val viewParent = it.parent
            if (viewParent is LinearLayout) {
                val count: Int = viewParent.childCount
                var answerIndex = 0
                var isNotEmptyWholeValidation: Boolean = true
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

                        surveyViewModel.answers[answerIndex++].apply {
                            this.body = textInputLayout.editText?.text.toString()
                        }

//                        Log.d("TAG", "addButton $answerIndex: ${textInputLayout.editText?.text}")
                    }
                }
                Log.d(
                    "TAG",
                    "addButton: ${surveyViewModel.answers} boolean: $isNotEmptyWholeValidation"
                )
            }
        }
        return button
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        surveyViewModel.farmerId = -1
    }
}