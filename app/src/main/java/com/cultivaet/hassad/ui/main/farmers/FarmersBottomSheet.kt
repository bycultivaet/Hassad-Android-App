package com.cultivaet.hassad.ui.main.farmers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cultivaet.hassad.databinding.FarmersBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FarmersBottomSheet(
    private val farmersList: List<FarmerDataItem>
) : BottomSheetDialogFragment() {

    private var _binding: FarmersBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FarmersBottomSheetBinding.inflate(inflater, container, false)

        val arrayAdapter = activity?.let { FarmersAdapter(it, farmersList) }
        binding.farmersRecyclerView.adapter = arrayAdapter

        binding.closeButton.setOnClickListener { this.dismiss() }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchStr = s.toString()
                val filteredList = farmersList.filter {
                    it.fullName.contains(searchStr) ||
                            it.phoneNumber.contains(searchStr)
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