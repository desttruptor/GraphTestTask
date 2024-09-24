package com.pnvpnvpnv.graphtesttask.view.table

sealed class TableElement {
    data class Header(
        val col1Header: String? = null,
        val col2Header: String? = null,
    ) : TableElement()

    data class Row(
        val rowNumber: Int,
        val col1Value: String,
        val col2Value: String,
    ) : TableElement()
}