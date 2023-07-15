package com.cultivaet.hassad.core.di

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val appComponent = listOf(
    networkModule,
//    databaseModule,
    helperModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)