package com.cultivaet.hassad.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.core.extension.logoutAlert
import com.cultivaet.hassad.databinding.ActivityMainBinding
import com.cultivaet.hassad.ui.BaseActivity
import com.cultivaet.hassad.ui.auth.LoginActivity
import com.cultivaet.hassad.ui.profile.ProfileActivity
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var location: Location? = null

    private val locationPermissionCode = 2

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 4 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_survey,
                R.id.fragment_tasks,
                R.id.fragment_farmers,
                R.id.fragment_content
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                launchActivity<ProfileActivity>()
            }

            R.id.logout -> {
                this@MainActivity.logoutAlert {
                    mainViewModel.loggedInState {
                        launchActivity<LoginActivity>(withFinish = true)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
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
            locationPermissionCode
        )
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                val locationRequest = LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                    interval = 5000 // Update interval in milliseconds
                    maxWaitTime = 15000 // Maximum time to wait for a location fix
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            val lastLocation = locationResult.lastLocation
                            location = lastLocation
                            Log.d(
                                "MainActivity",
                                "onLocationResult: ${lastLocation.latitude}, ${lastLocation.longitude}"
                            )
                        }
                    },
                    null
                )
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permissionDeniedMsg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getLocation(): Location? {
        return this.location
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}