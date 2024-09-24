package com.pnvpnvpnv.graphtesttask.screens.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnvpnvpnv.graphtesttask.network.PointsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GraphScreenViewModel(
    private val pointsRepository: PointsRepository
) : ViewModel() {

    val state: StateFlow<GraphScreenState> get() = _state
    private val _state = MutableStateFlow(GraphScreenState())

    val effect: Flow<GraphScreenEffect> get() = _effect.receiveAsFlow()
    private val _effect = Channel<GraphScreenEffect>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun loadPoints(count: Int) {
        viewModelScope.launch {
            _effect.send(GraphScreenEffect.ShowLoading(true))
            runCatching { pointsRepository.getPoints(count) }
                .onFailure { _effect.send(GraphScreenEffect.ShowError) }
                .onSuccess { resp ->
                    _state.update { prevState ->
                        prevState.copy(
                            pointsForTable = resp.mapForTable(),
                            pointsForGraph = resp.mapForGraph(),
                        )
                    }
                }
                .also { _effect.send(GraphScreenEffect.ShowLoading(false)) }
        }
    }
}