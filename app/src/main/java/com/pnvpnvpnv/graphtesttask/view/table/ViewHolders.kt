package com.pnvpnvpnv.graphtesttask.view.table

import androidx.recyclerview.widget.RecyclerView
import com.pnvpnvpnv.graphtesttask.databinding.RecyclerElementHeaderBinding
import com.pnvpnvpnv.graphtesttask.databinding.RecyclerElementRowBinding

class HeaderViewHolder(
    private val binding: RecyclerElementHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(element: TableElement.Header) {
        if (element.col1Header != null && element.col2Header != null) {
            binding.col1.text = element.col1Header
            binding.col2.text = element.col2Header
        }
    }
}

class RowViewHolder(
    private val binding: RecyclerElementRowBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(element: TableElement.Row) {
        binding.rowNumber.text = element.rowNumber.toString()
        binding.col1.text = element.col1Value
        binding.col2.text = element.col2Value
    }
}