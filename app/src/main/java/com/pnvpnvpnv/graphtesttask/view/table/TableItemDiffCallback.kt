package com.pnvpnvpnv.graphtesttask.view.table

import androidx.recyclerview.widget.DiffUtil

class TableItemDiffCallback : DiffUtil.ItemCallback<TableElement>() {
    override fun areItemsTheSame(oldItem: TableElement, newItem: TableElement): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: TableElement, newItem: TableElement): Boolean {
        return when {
            oldItem is TableElement.Header && newItem is TableElement.Header -> oldItem.col1Header == newItem.col1Header && oldItem.col2Header == newItem.col2Header
            oldItem is TableElement.Row && newItem is TableElement.Row -> oldItem.rowNumber == newItem.rowNumber
            else -> false
        }
    }
}