package com.cultivaet.hassad.core.source.remote

import com.cultivaet.hassad.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiBuilder {
    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    object Companion {
        fun apiService(): ApiService = retrofit().create(ApiService::class.java)
    }
}