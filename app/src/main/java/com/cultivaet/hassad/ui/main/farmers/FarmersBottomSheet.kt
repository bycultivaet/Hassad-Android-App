package com.cultivaet.hassad.ui.main.farmers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cultivaet.hassad.databinding.FarmersBottomSheetBinding
import com.cultivaet.hassad.domain.model.remote.responses.Farmer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FarmersBottomSheet(
    private val farmersList: List<Farmer>
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

        val arrayAdapter = FarmersAdapter(farmersList)
        binding.farmersRecyclerView.adapter = arrayAdapter

        binding.closeButton.setOnClickListener { this.dismiss() }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = farmersList.filter { it.firstName.contains(s.toString()) }
                arrayAdapter.setItems(filteredList)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}