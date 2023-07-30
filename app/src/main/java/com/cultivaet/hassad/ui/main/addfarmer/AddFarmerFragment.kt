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
import com.cultivaet.hassad.databinding.FragmentAddFarmerBinding
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

                    is AddFarmerState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.numberOfFarmersTextView.text =
                            getString(R.string.numberOfFarmersInList, it.farmers?.size)
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