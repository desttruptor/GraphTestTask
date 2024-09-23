package com.pnvpnvpnv.graphtesttask.network

import com.google.gson.annotations.SerializedName

data class PointsResponse(
    @SerializedName("points")
    val points: List<PointsItem>? = null
)

// assuming we're guarantied to have non-null values for simplicity
data class PointsItem(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double,
)
