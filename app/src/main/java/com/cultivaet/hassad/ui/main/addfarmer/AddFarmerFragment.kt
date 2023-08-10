package com.cultivaet.hassad.ui.main.addfarmer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.fillListOfTypesToAdapter
import com.cultivaet.hassad.core.extension.getDateFromString
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.FragmentAddFarmerBinding
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.ui.main.addfarmer.intent.AddFarmerIntent
import com.cultivaet.hassad.ui.main.addfarmer.viewstate.AddFarmerState
import com.cultivaet.hassad.ui.main.farmers.FarmersBottomSheet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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

    private var client: FusedLocationProviderClient? = null

    private var currentLocationLatitudeAndLongitude: String? = null

    private val listOfChecks = mutableListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentAddFarmerBinding.inflate(inflater, container, false)

            observeViewModel()

            client = LocationServices.getFusedLocationProviderClient(requireContext())

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
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    // When permission is granted
                    // Call method
                    getCurrentLocation()
                } else {
                    // When permission is not granted
                    // Call method
                    requestPermissions(
                        arrayOf<String>(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        100
                    )
                }

                Log.d("TAG", "onCreateView: $currentLocationLatitudeAndLongitude")

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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
        // Check condition
        if (requestCode == 100 && grantResults.isNotEmpty()
            && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)
        ) {
            // When permission are granted
            // Call  method
            getCurrentLocation()
        } else {
            // When permission are denied
            // Display toast
            Toast.makeText(
                activity,
                getString(R.string.permissionDeniedMsg),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        // Initialize Location manager
        val locationManager = activity
            ?.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
            || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            // When location service is enabled
            // Get last location
            client?.lastLocation?.addOnCompleteListener { task ->
                // Initialize location
                val location: Location? = task.result
                // Check condition
                if (location != null) {
                    // When location result is not
                    // null set latitude
                    currentLocationLatitudeAndLongitude = "${location.latitude},${location.longitude}"
                } else {
                    // When location result is null
                    // initialize location request
                    val locationRequest: LocationRequest = LocationRequest()
                        .setPriority(
                            LocationRequest.PRIORITY_HIGH_ACCURACY
                        )
                        .setInterval(10000)
                        .setFastestInterval(
                            1000
                        )
                        .setNumUpdates(1)

                    // Initialize location call back
                    val locationCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(
                            locationResult: LocationResult
                        ) {
                            // Initialize
                            // location
                            val location1 = locationResult
                                .lastLocation
                            // Set latitude
                            currentLocationLatitudeAndLongitude =
                                "${location1.latitude},${location1.longitude}"
                        }
                    }

                    // Request location updates
                    client?.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            }
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
                    .setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )
            )
        }
    }
}