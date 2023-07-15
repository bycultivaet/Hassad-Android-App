package com.cultivaet.hassad.core.di

import com.cultivaet.hassad.core.source.local.DatabaseBuilder
import com.cultivaet.hassad.core.source.remote.ApiBuilder
import com.cultivaet.hassad.data.repository.LoginRepositoryImpl
import com.cultivaet.hassad.data.source.local.DatabaseHelper
import com.cultivaet.hassad.data.source.local.DatabaseHelperImpl
import com.cultivaet.hassad.data.source.remote.ApiHelper
import com.cultivaet.hassad.data.source.remote.ApiHelperImpl
import com.cultivaet.hassad.domain.repository.LoginRepository
import com.cultivaet.hassad.domain.usecase.LoginUseCase
import com.cultivaet.hassad.ui.auth.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

val networkModule = module {
    single { ApiBuilder.Companion.apiService() }
}

val databaseModule = module {
    single { DatabaseBuilder.Companion.appDatabase(get()) }
}

val helperModule = module {
    single<ApiHelper> { ApiHelperImpl(get()) }
    single<DatabaseHelper> { DatabaseHelperImpl(get()) }
}

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { LoginUseCase(get()) }
}

@ExperimentalCoroutinesApi
val viewModelModule = module {
    factory { LoginViewModel(get()) }
}