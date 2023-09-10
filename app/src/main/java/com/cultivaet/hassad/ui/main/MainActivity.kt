package com.cultivaet.hassad.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
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
import com.cultivaet.hassad.ui.main.farmers.FarmersOfflineListener
import com.cultivaet.hassad.ui.main.intent.MainIntent
import com.cultivaet.hassad.ui.main.survey.SurveyOfflineListener
import com.cultivaet.hassad.ui.main.viewstate.MainState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity(), LocationListener {
    val mainViewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var locationManager: LocationManager

    private var location: Location? = null

    private val locationPermissionCode = 2

    private lateinit var surveyOfflineListener: SurveyOfflineListener

    private lateinit var farmersOfflineListener: FarmersOfflineListener

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("networkCallback", "onAvailable: ")

            runBlocking {
                lifecycleScope.launch { mainViewModel.mainIntent.send(MainIntent.SubmitOfflineFacilitatorAnswersList) }
            }

            runBlocking {
                lifecycleScope.launch { mainViewModel.mainIntent.send(MainIntent.SubmitOfflineFarmersList) }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("networkCallback", "onLost: ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 4 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_survey,
                R.id.fragment_tasks,
                R.id.fragment_farmers,
                R.id.fragment_content,
                R.id.fragment_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val connectivityManager = getSystemService(
            ConnectivityManager::class.java
        ) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    MainState.Idle -> {}

                    MainState.SuccessSurvey -> surveyOfflineListener.refreshFarmers()

                    MainState.SuccessFarmers -> farmersOfflineListener.refreshFarmers()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        val locationManager: LocationManager = getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5f, this)
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

    override fun onLocationChanged(location: Location) {
        Log.d(
            "MainActivity",
            "onLocationChanged: Latitude: ${location.latitude}, Longitude: ${location.longitude}"
        )
        this.location = location
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    fun getLocation(): Location? {
        return this.location
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    fun setOfflineListener(surveyOfflineListener: SurveyOfflineListener) {
        this.surveyOfflineListener = surveyOfflineListener
    }

    fun setOfflineListener(farmersOfflineListener: FarmersOfflineListener) {
        this.farmersOfflineListener = farmersOfflineListener
    }

    fun getUserId() = mainViewModel.userId
}