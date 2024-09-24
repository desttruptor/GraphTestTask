package com.pnvpnvpnv.graphtesttask.view.table

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pnvpnvpnv.graphtesttask.databinding.RecyclerElementHeaderBinding
import com.pnvpnvpnv.graphtesttask.databinding.RecyclerElementRowBinding

class PointsTableAdapter :
    ListAdapter<TableElement, RecyclerView.ViewHolder>(TableItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                binding = RecyclerElementHeaderBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_ROW -> RowViewHolder(
                binding = RecyclerElementRowBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            else -> error("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TableElement.Header -> (holder as HeaderViewHolder).bind(item)
            is TableElement.Row -> (holder as RowViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TableElement.Header -> VIEW_TYPE_HEADER
            is TableElement.Row -> VIEW_TYPE_ROW
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ROW = 1
    }
}