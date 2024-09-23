package com.pnvpnvpnv.graphtesttask.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApi {
    @GET("api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsResponse
}