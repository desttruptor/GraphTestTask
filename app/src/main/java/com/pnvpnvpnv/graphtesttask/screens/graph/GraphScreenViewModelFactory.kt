package com.pnvpnvpnv.graphtesttask.screens.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pnvpnvpnv.graphtesttask.network.PointsRepository

class GraphScreenViewModelFactory(
    private val pointsRepository: PointsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GraphScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GraphScreenViewModel(pointsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}