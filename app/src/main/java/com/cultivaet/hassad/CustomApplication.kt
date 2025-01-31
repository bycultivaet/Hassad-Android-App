package com.cultivaet.hassad

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cultivaet.hassad.core.di.appComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidLogger()
            androidContext(this@CustomApplication)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}