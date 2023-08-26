package com.cultivaet.hassad.ui.main.farmers.addfarmer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.core.extension.getDateFromString
import com.cultivaet.hassad.core.extension.isConnectedToInternet
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.FragmentAddFarmerBinding
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.farmers.addfarmer.intent.AddFarmerIntent
import com.cultivaet.hassad.ui.main.farmers.addfarmer.viewstate.AddFarmerState
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

    private val listOfChecks = mutableListOf<Boolean>()

    private var isNotEmptyWholeValidation = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

            observeViewModel()

            (activity as MainActivity).getCurrentLocation()

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

            binding.buttonAddFarmer.setOnClickListener {
                isNotEmptyWholeValidation = true

                val location = (activity as MainActivity).getLocation()
                if (location != null) {
                    Log.d("AddFarmerFragment", "locationAAA: $location")
                }

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
                listOfChecks.add(binding.dateTextField.showError(requireActivity()))
                listOfChecks.add(binding.cropTypeTextField.showError(requireActivity()))
                listOfChecks.add(binding.previouslyGrownCropsTextField.showError(requireActivity()))

                for (check in listOfChecks)
                    isNotEmptyWholeValidation = isNotEmptyWholeValidation && check

                if (location != null) {
                    if (isNotEmptyWholeValidation) {
                        addFarmerViewModel.farmer = Farmer(
                            firstName = firstName,
                            lastName = lastName,
                            phoneNumber = phoneNumber,
                            gender = gender,
                            age = age.toInt(),
                            address = address,
                            landArea = areaLand.toDouble(),
                            ownership = if (possessionType == getString(R.string.ownershipLand)) "owned" else "rented",
                            geolocation = "${location.latitude}, ${location.longitude}",
                            ZeroDay = selectedDate.getDateFromString(),
                            cropType = cropType,
                            cropsHistory = previouslyGrownCrops,
                            facilitatorId = addFarmerViewModel.userId
                        )

                        Log.d("AddFarmerFragment", "farmer: ${addFarmerViewModel.farmer}")

                        if (requireActivity().isConnectedToInternet()) {
                            runBlocking {
                                lifecycleScope.launch {
                                    addFarmerViewModel.addFarmerIntent.send(
                                        AddFarmerIntent.AddFarmer
                                    )
                                }
                            }
                        } else {
                            runBlocking {
                                lifecycleScope.launch {
                                    addFarmerViewModel.addFarmerIntent.send(
                                        AddFarmerIntent.InsertFarmerOffline
                                    )
                                }
                            }
                            responseMessageForFarmer(isOffline = true)
                        }
                    }
                } else {
                    (activity as MainActivity).getCurrentLocation()
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
                    }

                    is AddFarmerState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AddFarmerState.Success -> {
                        binding.progressBar.visibility = View.GONE

                        responseMessageForFarmer(isOffline = false)

                        findNavController().popBackStack()
                    }

                    is AddFarmerState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun responseMessageForFarmer(isOffline: Boolean = false) {
        if (isOffline) {
            Toast.makeText(activity, getString(R.string.addFarmerMsgOffline), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(activity, getString(R.string.added_successfully), Toast.LENGTH_SHORT)
                .show()
        }

        findNavController().popBackStack()
    }
}