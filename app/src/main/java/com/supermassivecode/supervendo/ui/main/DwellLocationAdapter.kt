package com.supermassivecode.supervendo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.supermassivecode.supervendo.R
import java.time.format.DateTimeFormatter

class DwellLocationAdapter :
    ListAdapter<DwellLocationUiModel, DwellLocationAdapter.ViewHolder>(DwellDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val locationLabel: TextView = view.findViewById(R.id.locationLabel)
        private val duration: TextView = view.findViewById(R.id.duration)
        private val timeRange: TextView = view.findViewById(R.id.timeRange)

        @SuppressLint("SetTextI18n")
        fun bind(item: DwellLocationUiModel) {
            val formatter = DateTimeFormatter.ofPattern("h:mm a")

            locationLabel.text = item.locationLabel
            duration.text = "Duration: ${item.durationMinutes} minutes"
            timeRange.text = "${item.startTime.format(formatter)} – ${item.endTime.format(formatter)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dwell_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DwellDiffCallback : DiffUtil.ItemCallback<DwellLocationUiModel>() {
        override fun areItemsTheSame(oldItem: DwellLocationUiModel, newItem: DwellLocationUiModel): Boolean {
            return oldItem.startTime == newItem.startTime && oldItem.endTime == newItem.endTime
        }

        override fun areContentsTheSame(oldItem: DwellLocationUiModel, newItem: DwellLocationUiModel): Boolean {
            return oldItem == newItem
        }
    }
}

