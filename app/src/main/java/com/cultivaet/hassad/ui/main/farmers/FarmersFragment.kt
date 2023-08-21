package com.cultivaet.hassad.ui.main.farmers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cultivaet.hassad.R
import com.cultivaet.hassad.databinding.FragmentFarmersBinding
import com.cultivaet.hassad.ui.main.farmers.intent.FarmersIntent
import com.cultivaet.hassad.ui.main.farmers.viewstate.FarmersState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class FarmersFragment : Fragment() {
    private val farmersViewModel: FarmersViewModel by inject()

    private var _binding: FragmentFarmersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val farmersAdapter: FarmersAdapter by lazy {
        FarmersAdapter(
            requireActivity(),
            listOf(),
            selectedFarmerId = -1,
            isSelectedOption = false
        ) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentFarmersBinding.inflate(inflater, container, false)

            observeViewModel()

            binding.farmersRecyclerView.adapter = farmersAdapter

            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val searchStr = s.toString()
                    val filteredList = farmersViewModel.farmersList?.filter {
                        it.fullName.contains(searchStr) || it.phoneNumber.contains(searchStr)
                    }
                    if (filteredList != null) {
                        farmersAdapter.setItems(filteredList)
                    }
                }
            })

            binding.fab.setOnClickListener { _ ->
                findNavController().navigate(R.id.to_add_farmer_fragment)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { farmersViewModel.farmersIntent.send(FarmersIntent.GetUserId) }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            farmersViewModel.state.collect {
                when (it) {
                    is FarmersState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is FarmersState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is FarmersState.Success -> {
                        binding.progressBar.visibility = View.GONE

                        if (it.data?.isNotEmpty() == true) {
                            binding.noFarmers.visibility = View.GONE
                            binding.farmersLinearLayout.visibility = View.VISIBLE

                            farmersAdapter.setItems(it.data)
                        }
                    }

                    is FarmersState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}