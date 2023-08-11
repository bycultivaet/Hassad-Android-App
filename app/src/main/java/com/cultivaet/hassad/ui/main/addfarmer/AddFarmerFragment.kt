package com.cultivaet.hassad.ui.main.addfarmer

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.core.extension.getDateFromString
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.FragmentAddFarmerBinding
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.ui.main.FragmentRefreshListener
import com.cultivaet.hassad.ui.main.MainActivity
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

    private var currentLocationLatitudeAndLongitude: String? = null

    private val listOfChecks = mutableListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

            observeViewModel()

//            (activity as MainActivity).geoLocation.observe(requireActivity()) { geoLocation ->
//                Log.d(
//                    "TAG: AddFarmerFragment",
//                    "onLocationChanged: Latitude: ${geoLocation.latitude}, Longitude: ${geoLocation.longitude}"
//                )
//            }

            (activity as MainActivity).setFragmentRefreshListener(object : FragmentRefreshListener {
                override fun onLocationChanged(location: Location) {
                    Log.d(
                        "TAG: SurveyFragment",
                        "onLocationChanged: Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                    )
                }
            })

            runBlocking {
                lifecycleScope.launch { addFarmerViewModel.addFarmerIntent.send(AddFarmerIntent.GetUserId) }
            }

            binding.genderTextField.fillListOfTypesToAdapter(
                requireContext(), listOf(getString(R.string.male), getString(R.string.female))
            )

            binding.possessionTypeTextField.fillListOfTypesToAdapter(
                requireContext(), listOf(
                    getString(R.string.ownershipLand), getString(R.string.rentalLand)
                )
            )

            binding.dateTextFieldAction.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

                datePicker.apply {
                    show(
                        this@AddFarmerFragment.requireActivity().supportFragmentManager,
                        "DATE_PICKER"
                    )
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
                (activity as MainActivity).getCurrentLocation()

                val firstName = binding.firstNameTextField.editText?.text.toString()
                val lastName = binding.lastNameTextField.editText?.text.toString()
                val phoneNumber = binding.phoneNumberTextField.editText?.text.toString()
                val gender = binding.genderTextField.editText?.text.toString()
                val age = binding.ageTextField.editText?.text.toString()
                val address = binding.addressTextField.editText?.text.toString()
                val possessionType = binding.possessionTypeTextField.editText?.text.toString()
                val areaLand = binding.areaLandTextField.editText?.text.toString()
                val selectedDate = binding.dateTextField.editText?.text.toString()
                val cropType = binding.cropTypeTextField.editText?.text.toString()
                val previouslyGrownCrops =
                    binding.previouslyGrownCropsTextField.editText?.text.toString()

                listOfChecks.clear()
                listOfChecks.add(binding.firstNameTextField.showError(requireActivity()))
                listOfChecks.add(binding.lastNameTextField.showError(requireActivity()))
                listOfChecks.add(binding.phoneNumberTextField.showError(requireActivity()))
                listOfChecks.add(binding.genderTextField.showError(requireActivity()))
                listOfChecks.add(binding.ageTextField.showError(requireActivity()))
                listOfChecks.add(binding.addressTextField.showError(requireActivity()))
                listOfChecks.add(binding.possessionTypeTextField.showError(requireActivity()))
                listOfChecks.add(binding.areaLandTextField.showError(requireActivity()))
                listOfChecks.add(currentLocationLatitudeAndLongitude != null)
                listOfChecks.add(binding.dateTextField.showError(requireActivity()))
                listOfChecks.add(binding.cropTypeTextField.showError(requireActivity()))
                listOfChecks.add(binding.previouslyGrownCropsTextField.showError(requireActivity()))

                var isValid = true
                for (check in listOfChecks)
                    isValid = isValid && check

                if (isValid) {
                    addFarmerViewModel.farmer = Farmer(
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phoneNumber,
                        gender = gender,
                        age = age.toInt(),
                        address = address,
                        landArea = areaLand.toDouble(),
                        ownership = if (possessionType == getString(R.string.ownershipLand)) "owned" else "rented",
                        geolocation = currentLocationLatitudeAndLongitude,
                        ZeroDay = selectedDate.getDateFromString(),
                        cropType = cropType,
                        cropsHistory = previouslyGrownCrops,
                        facilitatorId = addFarmerViewModel.userId
                    )

                    runBlocking {
                        lifecycleScope.launch {
                            addFarmerViewModel.addFarmerIntent.send(
                                AddFarmerIntent.AddFarmer
                            )
                        }
                    }
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
                        if (it.data is List<*>) binding.numberOfFarmersTextView.text =
                            getString(R.string.numberOfFarmersInList, it.data.size)
                        else {
                            Toast.makeText(
                                activity, getString(R.string.added_successfully), Toast.LENGTH_SHORT
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
}