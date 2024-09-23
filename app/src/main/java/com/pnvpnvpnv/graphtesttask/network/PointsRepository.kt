package com.pnvpnvpnv.graphtesttask.network

interface PointsRepository {
    suspend fun getPoints(count: Int): PointsResponse
}