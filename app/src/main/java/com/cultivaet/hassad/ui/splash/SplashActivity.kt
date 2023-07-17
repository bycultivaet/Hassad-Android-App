package com.cultivaet.hassad.ui.splash

import android.os.Bundle
import com.cultivaet.hassad.core.extension.launchActivity
import com.cultivaet.hassad.databinding.ActivitySplashBinding
import com.cultivaet.hassad.ui.BaseActivity
import com.cultivaet.hassad.ui.auth.LoginActivity
import com.cultivaet.hassad.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity() {
    private val splashViewModel: SplashViewModel by inject()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel.handleLoggedInState { isLoggedIn ->
            if (isLoggedIn == true) {
                launchActivity<MainActivity>(
                    withFinish = true
                )
            } else {
                launchActivity<LoginActivity>(
                    withFinish = true
                )
            }
        }
    }
}