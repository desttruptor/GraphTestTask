package com.pnvpnvpnv.graphtesttask.screens.graph

import android.graphics.PointF
import com.pnvpnvpnv.graphtesttask.network.PointsResponse
import com.pnvpnvpnv.graphtesttask.view.table.TableElement

fun PointsResponse.mapForTable(): List<TableElement> {
    val points = this.points
        ?.sortedBy { it.x }
        ?.mapIndexed { index, point ->
            TableElement.Row(
                rowNumber = index,
                col1Value = point.x.toString(),
                col2Value = point.y.toString(),
            )
        } ?: emptyList()
    val header = listOf(TableElement.Header())
    return buildList<TableElement> {
        addAll(header)
        addAll(points)
    }
}

fun PointsResponse.mapForGraph(): List<PointF> = this.points
    ?.map { PointF(it.x.toFloat(), it.y.toFloat()) }
    ?.sortedBy { it.x }
    .orEmpty()