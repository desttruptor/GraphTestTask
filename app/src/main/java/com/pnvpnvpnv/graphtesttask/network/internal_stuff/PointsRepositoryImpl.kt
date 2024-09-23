package com.pnvpnvpnv.graphtesttask.network.internal_stuff

import com.pnvpnvpnv.graphtesttask.network.PointsApi
import com.pnvpnvpnv.graphtesttask.network.PointsRepository
import com.pnvpnvpnv.graphtesttask.network.PointsResponse

class PointsRepositoryImpl(
    private val api: PointsApi,
) : PointsRepository {
    override suspend fun getPoints(count: Int): PointsResponse {
        return api.getPoints(count)
    }
}