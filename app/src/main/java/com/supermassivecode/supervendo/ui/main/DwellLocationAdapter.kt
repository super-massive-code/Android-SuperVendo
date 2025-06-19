package com.supermassivecode.supervendo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.supermassivecode.supervendo.R
import java.time.format.DateTimeFormatter

class DwellLocationAdapter(
    private var items: List<DwellLocationUiModel>
) :
    RecyclerView.Adapter<DwellLocationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationLabel: TextView = view.findViewById(R.id.locationLabel)
        val duration: TextView = view.findViewById(R.id.duration)
        val timeRange: TextView = view.findViewById(R.id.timeRange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dwell_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val formatter = DateTimeFormatter.ofPattern("h:mm a")

        holder.locationLabel.text = item.locationLabel
        holder.duration.text = "Duration: ${item.durationMinutes} minutes"
        holder.timeRange.text = "${item.startTime.format(formatter)} – ${item.endTime.format(formatter)}"
    }

    fun updateData(newItems: List<DwellLocationUiModel>) {
        items = newItems
        this.notifyDataSetChanged()
    }
}
