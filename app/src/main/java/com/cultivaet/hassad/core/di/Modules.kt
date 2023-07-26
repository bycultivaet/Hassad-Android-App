package com.cultivaet.hassad.core.di

import com.cultivaet.hassad.core.source.local.DatabaseBuilder
import com.cultivaet.hassad.core.source.local.datastore.DataStorePreferences
import com.cultivaet.hassad.core.source.local.datastore.PreferencesDataSource
import com.cultivaet.hassad.core.source.remote.ApiBuilder
import com.cultivaet.hassad.data.repository.AddFarmerRepositoryImpl
import com.cultivaet.hassad.data.repository.LoginRepositoryImpl
import com.cultivaet.hassad.data.repository.MainRepositoryImpl
import com.cultivaet.hassad.data.repository.ProfileRepositoryImpl
import com.cultivaet.hassad.data.repository.SplashRepositoryImpl
import com.cultivaet.hassad.data.source.local.DatabaseHelper
import com.cultivaet.hassad.data.source.local.DatabaseHelperImpl
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.data.source.remote.ApiHelperImpl
import com.cultivaet.hassad.domain.repository.AddFarmerRepository
import com.cultivaet.hassad.domain.repository.LoginRepository
import com.cultivaet.hassad.domain.repository.MainRepository
import com.cultivaet.hassad.domain.repository.ProfileRepository
import com.cultivaet.hassad.domain.repository.SplashRepository
import com.cultivaet.hassad.domain.usecase.AddFarmerUseCase
import com.cultivaet.hassad.domain.usecase.LoginUseCase
import com.cultivaet.hassad.domain.usecase.MainUseCase
import com.cultivaet.hassad.domain.usecase.ProfileUseCase
import com.cultivaet.hassad.domain.usecase.SplashUseCase
import com.cultivaet.hassad.ui.auth.LoginViewModel
import com.cultivaet.hassad.ui.main.MainViewModel
import com.cultivaet.hassad.ui.main.addfarmer.AddFarmerViewModel
import com.cultivaet.hassad.ui.main.profile.ProfileViewModel
import com.cultivaet.hassad.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

val networkModule = module {
    single { ApiBuilder.Companion.apiService() }
}

val databaseModule = module {
    single<PreferencesDataSource> { DataStorePreferences(get()) }
    single { DatabaseBuilder.Companion.appDatabase(get()) }
}

val helperModule = module {
    single<ApiHelper> { ApiHelperImpl(get()) }
    single<DatabaseHelper> { DatabaseHelperImpl(get()) }
}

val repositoryModule = module {
    single<SplashRepository> { SplashRepositoryImpl(get()) }
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    single<MainRepository> { MainRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<AddFarmerRepository> { AddFarmerRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    single { SplashUseCase(get()) }
    single { LoginUseCase(get()) }
    single { MainUseCase(get()) }
    single { ProfileUseCase(get()) }
    single { AddFarmerUseCase(get()) }
}

@ExperimentalCoroutinesApi
val viewModelModule = module {
    factory { SplashViewModel(get()) }
    factory { LoginViewModel(get(), get()) }
    factory { MainViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { AddFarmerViewModel(get()) }
}