package com.cultivaet.hassad

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
open class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CustomApplication)
//            modules(provideDependency())
        }
    }

//    open fun provideDependency() = appComponent
}