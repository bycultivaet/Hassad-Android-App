package com.cultivaet.hassad.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cultivaet.hassad.R
import com.cultivaet.hassad.ui.auth.intent.LoginIntent
import com.cultivaet.hassad.ui.auth.viewstate.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpUi()
        observeViewModel()
    }

    private fun setUpUi() {
        runBlocking {
            lifecycleScope.launch { loginViewModel.loginIntent.send(LoginIntent.FetchFacilitator) }
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
                            it.facilitator.firstName,
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