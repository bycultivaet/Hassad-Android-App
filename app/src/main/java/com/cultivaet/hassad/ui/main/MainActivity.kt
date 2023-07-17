package com.cultivaet.hassad.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cultivaet.hassad.R
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.core.extension.logoutAlert
import com.cultivaet.hassad.databinding.ActivityMainBinding
import com.cultivaet.hassad.ui.BaseActivity
import com.cultivaet.hassad.ui.auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_poll,
                R.id.navigation_tasks,
                R.id.navigation_addition,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
}