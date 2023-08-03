package com.cultivaet.hassad.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.core.extension.showError
import com.cultivaet.hassad.databinding.ActivityLoginBinding
import com.cultivaet.hassad.ui.BaseActivity
import com.cultivaet.hassad.ui.auth.viewstate.LoginState
import com.cultivaet.hassad.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class LoginActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by inject()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.buttonLogin.setOnClickListener {
            val isNotEmptyPhoneNumber = binding.phoneNumberTextField.showError(
                this@LoginActivity,
                getString(R.string.phone_number)
            )
            if (isNotEmptyPhoneNumber)
                loginViewModel.login(binding.phoneNumberTextField.editText?.text.toString())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            loginViewModel.state.collect {
                when (it) {
                    is LoginState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is LoginState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is LoginState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        launchActivity<MainActivity>(withFinish = true)
                    }

                    is LoginState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}