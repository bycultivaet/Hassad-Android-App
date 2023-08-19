package com.cultivaet.hassad.ui.main.farmers.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cultivaet.hassad.R
import com.cultivaet.hassad.databinding.FarmersBottomSheetBinding
import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem
import com.cultivaet.hassad.ui.main.farmers.FarmersAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FarmersBottomSheet(
    private val farmersList: List<FarmerDataItem>,
    private val isSelectedOption: Boolean = false,
    private val selectedFarmerId: Int = -1,
    private val setFarmerId: (selectedFarmerId: Int?) -> Unit = {}
) : BottomSheetDialogFragment() {

    private var _binding: FarmersBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var farmerId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FarmersBottomSheetBinding.inflate(inflater, container, false)

        binding.listOfFarmers.text =
            getString(if (isSelectedOption) R.string.select_farmer_from_menu else R.string.listOfFarmers)

        binding.closeButton.setImageResource(if (isSelectedOption) R.drawable.ic_done else R.drawable.ic_close)

        val arrayAdapter = activity?.let {
            FarmersAdapter(it, farmersList, selectedFarmerId, isSelectedOption) { farmerId ->
                if (farmerId != null) {
                    this.farmerId = farmerId
                }
            }
        }
        binding.farmersRecyclerView.adapter = arrayAdapter

        binding.closeButton.setOnClickListener {
            setFarmerId.invoke(farmerId)
            this.dismiss()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchStr = s.toString()
                val filteredList = farmersList.filter {
                    it.fullName.contains(searchStr) || it.phoneNumber.contains(searchStr)
                }
                arrayAdapter?.setItems(filteredList)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}