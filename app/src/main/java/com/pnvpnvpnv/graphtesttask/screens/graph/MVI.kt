package com.pnvpnvpnv.graphtesttask.screens.graph

import android.graphics.PointF
import com.pnvpnvpnv.graphtesttask.view.table.TableElement

data class GraphScreenState(
    val pointsForGraph: List<PointF>? = null,
    val pointsForTable: List<TableElement>? = null
)

sealed interface GraphScreenEffect {
    data object ShowError : GraphScreenEffect
    data object NetworkError : GraphScreenEffect
    data class ShowLoading(val show: Boolean) : GraphScreenEffect
}