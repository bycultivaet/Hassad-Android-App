package com.cultivaet.hassad

import android.app.Application
import com.cultivaet.hassad.core.di.appComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CustomApplication)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}