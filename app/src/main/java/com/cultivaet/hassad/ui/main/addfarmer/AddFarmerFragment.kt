package com.cultivaet.hassad.ui.main.addfarmer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.getDateFromString
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.FragmentAddFarmerBinding
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.ui.main.addfarmer.intent.AddFarmerIntent
import com.cultivaet.hassad.ui.main.addfarmer.viewstate.AddFarmerState
import com.cultivaet.hassad.ui.main.farmers.FarmersBottomSheet
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class AddFarmerFragment : Fragment() {
    private val addFarmerViewModel: AddFarmerViewModel by inject()

    private var _binding: FragmentAddFarmerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

        observeViewModel()

        runBlocking {
            lifecycleScope.launch { addFarmerViewModel.addFarmerIntent.send(AddFarmerIntent.GetUserId) }
        }

        val genderTypes = listOf(getString(R.string.male), getString(R.string.female))
        val genderTypesAdapter = ArrayAdapter(requireContext(), R.layout.list_item, genderTypes)
        (binding.genderTextField.editText as? AutoCompleteTextView)?.setAdapter(genderTypesAdapter)

        val possessionTypes = listOf(
            getString(R.string.ownershipLand),
            getString(R.string.rentalLand)
        )
        val possessionTypesAdapter =
            ArrayAdapter(requireContext(), R.layout.list_item, possessionTypes)
        (binding.possessionTypeTextField.editText as? AutoCompleteTextView)?.setAdapter(
            possessionTypesAdapter
        )

        binding.dateTextFieldAction.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.apply {
                show(this@AddFarmerFragment.requireActivity().supportFragmentManager, "DATE_PICKER")
                addOnPositiveButtonClickListener {
                    binding.dateTextField.editText?.setText(this.headerText)
                }
            }
        }

        binding.listOfFarmers.setOnClickListener {
            val farmersBottomSheet = addFarmerViewModel.farmersList?.let { farmers ->
                FarmersBottomSheet(
                    farmers
                )
            }
            farmersBottomSheet?.show(parentFragmentManager, "farmersBottomSheet")
        }

        binding.buttonAddFarmer.setOnClickListener {
            val firstName = binding.firstNameTextField.editText?.text.toString()
            val lastName = binding.lastNameTextField.editText?.text.toString()
            val phoneNumber = binding.phoneNumberTextField.editText?.text.toString()
            val gender = binding.genderTextField.editText?.text.toString()
            val age = binding.ageTextField.editText?.text.toString()
            val address = binding.addressTextField.editText?.text.toString()
            val possessionType = binding.possessionTypeTextField.editText?.text.toString()
            val areaLand = binding.areaLandTextField.editText?.text.toString()
            val geographicalLocationEarth =
                binding.geographicalLocationEarthTextField.editText?.text.toString()
            val selectedDate = binding.dateTextField.editText?.text.toString()
            val currentCrop = binding.currentCropTextField.editText?.text.toString()
            val previouslyGrownCrops =
                binding.previouslyGrownCropsTextField.editText?.text.toString()

            val isNotEmptyFirstName = binding.firstNameTextField.showError(
                requireActivity(),
                getString(R.string.firstName)
            )

            val isNotEmptyLastName = binding.lastNameTextField.showError(
                requireActivity(),
                getString(R.string.lastName)
            )

            val isNotEmptyPhoneNumber = binding.phoneNumberTextField.showError(
                requireActivity(),
                getString(R.string.phone_number)
            )

            val isNotEmptyGender = binding.genderTextField.showError(
                requireActivity(),
                getString(R.string.gender)
            )

            val isNotEmptyAge = binding.ageTextField.showError(
                requireActivity(),
                getString(R.string.age)
            )

            val isNotEmptyAddress = binding.addressTextField.showError(
                requireActivity(),
                getString(R.string.address)
            )

            val isNotEmptyPossessionType = binding.possessionTypeTextField.showError(
                requireActivity(),
                getString(R.string.possessionType)
            )

            val isNotEmptyLandArea = binding.areaLandTextField.showError(
                requireActivity(),
                getString(R.string.landArea)
            )

            val isNotEmptyGeographicalLocationEarth =
                binding.geographicalLocationEarthTextField.showError(
                    requireActivity(),
                    getString(R.string.geographicalLocationEarth)
                )

            val isNotEmptyFirstDayToCultivation = binding.dateTextField.showError(
                requireActivity(),
                getString(R.string.firstDayToCultivation)
            )

            val isNotEmptyCurrentCrop = binding.currentCropTextField.showError(
                requireActivity(),
                getString(R.string.currentCrop)
            )

            val isNotEmptyPreviouslyGrownCropse = binding.previouslyGrownCropsTextField.showError(
                requireActivity(),
                getString(R.string.previouslyGrownCrops)
            )

            if (isNotEmptyFirstName && isNotEmptyLastName && isNotEmptyPhoneNumber &&
                isNotEmptyGender && isNotEmptyAge && isNotEmptyAddress &&
                isNotEmptyPossessionType && isNotEmptyLandArea && isNotEmptyGeographicalLocationEarth &&
                isNotEmptyFirstDayToCultivation && isNotEmptyCurrentCrop && isNotEmptyPreviouslyGrownCropse
            ) {
                addFarmerViewModel.farmer = Farmer(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber,
                    gender = gender,
                    age = age.toInt(),
                    address = address,
                    landArea = areaLand.toDouble(),
                    ownership = possessionType,
                    geolocation = "40.7128° N, 74.0060° W",
                    currentYield = currentCrop,
                    ZeroDay = selectedDate.getDateFromString(),
                    cropType = "annual",
                    cropsHistory = previouslyGrownCrops,
                    facilitatorId = addFarmerViewModel.userId
                )

                runBlocking {
                    lifecycleScope.launch { addFarmerViewModel.addFarmerIntent.send(AddFarmerIntent.AddFarmer) }
                }
            }
        }

        return binding.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            addFarmerViewModel.state.collect {
                when (it) {
                    is AddFarmerState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.numberOfFarmersTextView.text =
                            getString(R.string.numberOfFarmersInList, 0)
                    }

                    is AddFarmerState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AddFarmerState.Success<*> -> {
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

                            // TODO: back to home or clear editTexts
                            runBlocking {
                                lifecycleScope.launch {
                                    addFarmerViewModel.addFarmerIntent.send(
                                        AddFarmerIntent.GetUserId
                                    )
                                }
                            }
                        }
                    }

                    is AddFarmerState.Error -> {
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