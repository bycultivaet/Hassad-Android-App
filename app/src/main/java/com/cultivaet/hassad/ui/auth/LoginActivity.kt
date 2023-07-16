package com.cultivaet.hassad.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.databinding.ActivityLoginBinding
import com.cultivaet.hassad.ui.auth.viewstate.LoginState
import com.cultivaet.hassad.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
            loginViewModel.login(binding.outlinedTextField.editText?.text.toString())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            loginViewModel.state.collect {
                when (it) {
                    is LoginState.Idle -> {}

                    is LoginState.Loading -> {
//                        progressBar.visibility = View.VISIBLE
                    }

                    is LoginState.Success -> {
//                        progressBar.visibility = View.GONE
                        launchActivity<MainActivity>(withFinish = true)
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