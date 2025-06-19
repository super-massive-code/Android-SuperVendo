package com.supermassivecode.supervendo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supermassivecode.supervendo.R

class MainActivity : ComponentActivity() {

    private lateinit var adapter: DwellLocationAdapter
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DwellLocationAdapter(emptyList())

        val recyclerView = findViewById<RecyclerView>(R.id.dwellRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        val earningsText = findViewById<TextView>(R.id.earningsText)
        val locationsText = findViewById<TextView>(R.id.locationsVisitedText)
        val dateText = findViewById<TextView>(R.id.dateText)

        viewModel.dwellLocations.observe(this) { dwellList ->
            adapter.updateData(dwellList)
            locationsText.text = "${dwellList.size}"
        }

        viewModel.earningsToday.observe(this) { totalText ->
            earningsText.text = totalText
        }

        viewModel.todayDate.observe(this) { dateStr ->
            dateText.text = dateStr
        }
    }
}
