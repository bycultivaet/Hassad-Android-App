package com.cultivaet.hassad.ui.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
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

    private val permissionId = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        binding.buttonLogin.setOnClickListener {
            if (binding.phoneNumberTextField.showError(this@LoginActivity)) {
                val phoneNumber = binding.phoneNumberTextField.editText?.text.toString()
                if (checkPermissions()) {
                    if (isLocationEnabled()) {
                        loginViewModel.login(phoneNumber)
                    } else {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                } else {
                    requestPermissions()
                }
            }
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

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
}