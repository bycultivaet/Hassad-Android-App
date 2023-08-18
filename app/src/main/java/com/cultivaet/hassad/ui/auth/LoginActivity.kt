package com.cultivaet.hassad.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Locale

@ExperimentalCoroutinesApi
class LoginActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by inject()

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        observeViewModel()

        binding.buttonLogin.setOnClickListener {
            if (binding.phoneNumberTextField.showError(this@LoginActivity)) {
                if (checkPermissions() && isLocationEnabled()) {
                    loginViewModel.login(binding.phoneNumberTextField.editText?.text.toString())
                } else {
                    getCurrentLocation()
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

    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: MutableList<Address>? =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        address = list?.get(0)
                        Log.d("TAG", "getCurrentLocation: $address")
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
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

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            }
        }
    }
}