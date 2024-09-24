package com.pnvpnvpnv.graphtesttask.deps

import com.google.gson.GsonBuilder
import com.pnvpnvpnv.graphtesttask.deps.Constants.BASE_URL
import com.pnvpnvpnv.graphtesttask.network.PointsApi
import com.pnvpnvpnv.graphtesttask.network.PointsRepository
import com.pnvpnvpnv.graphtesttask.network.internal_stuff.PointsRepositoryImpl
import com.pnvpnvpnv.graphtesttask.screens.graph.GraphScreenViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DependencyContainer {
    // internal deps
    private val gson by lazy { GsonBuilder().create() }
    private val gsonConverterFactory by lazy { GsonConverterFactory.create(gson) }
    private val okHttpClient by lazy { OkHttpClient.Builder().build() }
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BASE_URL)
            .build()
    }
    private val pointsApi by lazy { retrofit.create(PointsApi::class.java) }
    private val pointsRepository: PointsRepository by lazy {
        PointsRepositoryImpl(pointsApi)
    }

    // external deps
    fun getGraphScreenViewModelFactory() = GraphScreenViewModelFactory(pointsRepository)
}