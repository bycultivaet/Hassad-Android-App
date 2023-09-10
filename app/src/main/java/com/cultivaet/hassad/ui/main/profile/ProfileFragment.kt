package com.cultivaet.hassad.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.databinding.FragmentProfileBinding
import com.cultivaet.hassad.ui.main.MainActivity
import com.cultivaet.hassad.ui.main.profile.intent.ProfileIntent
import com.cultivaet.hassad.ui.main.profile.viewstate.ProfileState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by inject()

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentProfileBinding.inflate(inflater, container, false)

            observeViewModel()

            profileViewModel.userId = (activity as MainActivity).getUserId()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            lifecycleScope.launch { profileViewModel.profileIntent.send(ProfileIntent.FetchFacilitator) }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            profileViewModel.state.collect {
                when (it) {
                    is ProfileState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is ProfileState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ProfileState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.firstNameTextField.editText?.setText(it.facilitator?.firstName)
                        binding.lastNameTextField.editText?.setText(it.facilitator?.lastName)
                        binding.phoneNumberTextField.editText?.setText(it.facilitator?.phoneNumber)
                        binding.genderTextField.editText?.setText(it.facilitator?.gender)
                        binding.ageTextField.editText?.setText(it.facilitator?.age.toString())
                        binding.addressTextField.editText?.setText(it.facilitator?.address)
                        binding.universityTextField.editText?.setText(it.facilitator?.university)
                        binding.majorTextField.editText?.setText(it.facilitator?.major)
                        binding.gradYearTextField.editText?.setText(it.facilitator?.gradYear.toString())
                        binding.experienceTextField.editText?.setText(it.facilitator?.experience.toString())
                        binding.hasVehicleTextField.editText?.setText(
                            it.facilitator?.answerOfHavingVehicle(
                                activity
                            )
                        )
                    }

                    is ProfileState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}