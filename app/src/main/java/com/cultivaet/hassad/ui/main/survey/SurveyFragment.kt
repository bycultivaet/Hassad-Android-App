package com.cultivaet.hassad.ui.main.survey

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.core.extension.getDateFromString
import com.cultivaet.hassad.core.extension.isConnectedToInternet
import com.cultivaet.hassad.core.extension.setMargin
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.core.util.Utils
import com.cultivaet.hassad.databinding.FragmentSurveyBinding
import com.cultivaet.hassad.domain.model.remote.requests.Answer
import com.cultivaet.hassad.domain.model.remote.responses.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Field
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.model.remote.responses.ImageUUID
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
class SurveyFragment : Fragment(), SurveyOfflineListener {
    private val surveyViewModel: SurveyViewModel by inject()

    private var _binding: FragmentSurveyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isNotEmptyWholeValidation = true

    private val REQUEST_CODE = 200

    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CODE)
        }
    }

    private var isThereImages: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentSurveyBinding.inflate(inflater, container, false)

            observeViewModel()

            surveyViewModel.facilitatorAnswer.userId = (activity as MainActivity).getUserId()

            (activity as MainActivity).setOfflineListener(this)

            (activity as MainActivity).getCurrentLocation()

            lifecycleScope.launch {
                surveyViewModel.surveyIntent.send(
                    SurveyIntent.GetFacilitatorForm
                )
            }

            binding.listOfFarmers.setOnClickListener {
                val farmersBottomSheet = surveyViewModel.farmersList?.let { farmers ->
                    FarmersBottomSheet(
                        farmers,
                        isSelectedOption = true,
                        surveyViewModel.facilitatorAnswer.farmerId
                    ) { farmerId ->
                        if (farmerId != null && farmerId != -1) {
                            surveyViewModel.facilitatorAnswer.farmerId = farmerId

                            (activity as MainActivity).getCurrentLocation()

                            renderDynamicViews(surveyViewModel.facilitatorForm)
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
        refreshFarmers()
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

                            is FacilitatorAnswer -> {
                                responseMessageForFacilitatorAnswer(isOffline = false)
                            }

                            is ImageUUID -> {
                                runBlocking {
                                    lifecycleScope.launch {
                                        surveyViewModel.surveyIntent.send(
                                            SurveyIntent.SubmitFacilitatorAnswer
                                        )
                                    }
                                }
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

    private fun responseMessageForFacilitatorAnswer(isOffline: Boolean = false) {
        if (isOffline) {
            Toast.makeText(activity, getString(R.string.surveyMsgOffline), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(activity, getString(R.string.added_successfully), Toast.LENGTH_SHORT)
                .show()
        }

        binding.scrollView.visibility = View.GONE
        binding.selectFarmerMsgTextView.visibility = View.VISIBLE

        if (!isOffline) {
            refreshFarmers()
        }
    }

    private fun renderDynamicViews(form: Form) {
        binding.rootContainer.removeAllViews()

        imagesAdapter.setItems(mutableListOf(Utils.dummyBitmap()))

        surveyViewModel.facilitatorAnswer.formId = form.ID
        form.fields.forEachIndexed { index, field ->
            val viewGroup = when (field.type) {
                "date" -> {
                    addConstraintLayout(field)
                }

                "images" -> {
                    isThereImages = true
                    addLinearLayoutForImages(field)
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

        binding.scrollView.visibility = View.VISIBLE
        binding.selectFarmerMsgTextView.visibility = View.GONE
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
                    textInputLayout.editText?.setText(it.getDateFromString())
                }
            }
        }
        return constraintLayout
    }

    private fun addLinearLayoutForImages(field: Field): LinearLayout {
        val linearLayout = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.text_input_layout_image,
                null
            ) as LinearLayout

        linearLayout.tag = field.name

        val imagesTitle = linearLayout.findViewById<TextView>(R.id.imagesTitle)

        imagesTitle.text = field.placeholder

        val imagesRecyclerView = linearLayout.findViewById<RecyclerView>(R.id.imagesRecyclerView)

        imagesRecyclerView.adapter = imagesAdapter

        return linearLayout
    }

    private fun addButton(): Button {
        val button = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.button_item,
                null
            ) as Button

        button.setOnClickListener {
            isNotEmptyWholeValidation = true

            surveyViewModel.uuidImages = ""

            val viewParent = it.parent
            if (viewParent is LinearLayout) {
                val count: Int = viewParent.childCount
                var answerIndex = 0
                for (index in 0 until count) {
                    val view: View = viewParent.getChildAt(index)
                    val textInputLayout = when (view) {
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
                    } else if (view is LinearLayout && isThereImages) {
                        surveyViewModel.indexOfImages = answerIndex++
                        surveyViewModel.facilitatorAnswer.answers[surveyViewModel.indexOfImages].apply {
                            this.body = Utils.toJson(imagesAdapter.getItems())
                        }
                    }
                }
            }

            val location = (activity as MainActivity).getLocation()
            if (location != null) {
                Log.d("SurveyFragment", "location: $location")

                surveyViewModel.facilitatorAnswer.geolocation =
                    "${location.latitude}, ${location.longitude}"

                Log.d(
                    "SurveyFragment",
                    "facilitatorAnswer: ${surveyViewModel.facilitatorAnswer}"
                )

                if (isThereImages) {
                    if (imagesAdapter.itemCount == 1) {
                        Toast.makeText(
                            activity,
                            getString(R.string.uploadAtleastImageMsg),
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    } else {
                        if (isNotEmptyWholeValidation) {
                            if (requireActivity().isConnectedToInternet()) {
                                runBlocking {
                                    val list =
                                        (activity as MainActivity).mainViewModel.getUUIDsFromBitmaps(
                                            imagesAdapter.getItems()
                                        ).await()

                                    surveyViewModel.facilitatorAnswer.answers[surveyViewModel.indexOfImages].apply {
                                        this.body =
                                            list.map { def -> def.await() }.joinToString(", ")
                                    }
                                }
                            }
                        }
                    }
                }

                Log.d("TAG", "addButtonAmr: ${surveyViewModel.facilitatorAnswer.answers}")

                if (isNotEmptyWholeValidation) {
                    if (requireActivity().isConnectedToInternet()) {
                        runBlocking {
                            lifecycleScope.launch {
                                surveyViewModel.surveyIntent.send(
                                    SurveyIntent.SubmitFacilitatorAnswer
                                )
                            }
                        }
                    } else {
                        runBlocking {
                            lifecycleScope.launch {
                                surveyViewModel.surveyIntent.send(
                                    SurveyIntent.InsertFacilitatorAnswerOffline
                                )
                            }
                        }
                        responseMessageForFacilitatorAnswer(isOffline = true)
                    }
                }
            } else {
                (activity as MainActivity).getCurrentLocation()
            }
        }
        return button
    }

    override fun refreshFarmers() {
        lifecycleScope.launch {
            surveyViewModel.surveyIntent.send(
                SurveyIntent.FetchAllFarmers
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            imagesAdapter.setItem(bitmap)
        }
    }
}