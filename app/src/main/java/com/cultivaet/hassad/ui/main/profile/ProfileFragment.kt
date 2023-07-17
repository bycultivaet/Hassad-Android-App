package com.cultivaet.hassad.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.databinding.FragmentProfileBinding
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeViewModel()

        runBlocking {
            lifecycleScope.launch { profileViewModel.profileIntent.send(ProfileIntent.GetUserId) }
        }

        return root
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
                        binding.textSettings.text = it.facilitator?.firstName
                    }

                    is ProfileState.Error -> {
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