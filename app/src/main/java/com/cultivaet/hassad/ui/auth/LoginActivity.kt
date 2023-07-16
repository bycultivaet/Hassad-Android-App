package com.cultivaet.hassad.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.databinding.ActivityLoginBinding
import com.cultivaet.hassad.ui.auth.intent.LoginIntent
import com.cultivaet.hassad.ui.auth.viewstate.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.buttonLogin.setOnClickListener {
            loginViewModel.phoneNumber = binding.outlinedTextField.editText?.text.toString()
            if (loginViewModel.phoneNumber.isNotEmpty()) {
                runBlocking {
                    lifecycleScope.launch { loginViewModel.loginIntent.send(LoginIntent.FetchFacilitator) }
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.please_enter_require_data), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            loginViewModel.state.collect {
                when (it) {
                    is LoginState.Idle -> {

                    }

                    is LoginState.Loading -> {
//                        progressBar.visibility = View.VISIBLE
                    }

                    is LoginState.Success -> {
//                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            it.facilitator?.firstName,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is LoginState.Error -> {
//                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}