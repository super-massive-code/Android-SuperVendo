package com.supermassivecode.supervendo

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.supermassivecode.supervendo.data.GpsPoint
import com.supermassivecode.supervendo.data.TrackerRepo
import androidx.lifecycle.viewModelScope
import com.supermassivecode.supervendo.data.Earnings
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = TrackerRepo(application.applicationContext)

    fun recordLocation(location: Location) {
        if (location.accuracy < 50 && location.latitude != 0.0 && location.longitude != 0.0) {
            viewModelScope.launch {
                repo.insertGpsPoint(
                    GpsPoint(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        speed = location.speed
                    )
                )
                repo.detectAndInsertDwell()
            }
        }
    }

    fun seedDummyData() {
        viewModelScope.launch {
            // Insert fake GPS points (simulate stationary period)
            val now = LocalDateTime.now()
            val dummyPoints = List(10) { i ->
                GpsPoint(
                    timestamp = now.minusMinutes((10 - i).toLong()),
                    latitude = 37.7749,
                    longitude = -122.4194,
                    speed = 0.5f
                )
            }
            dummyPoints.forEach { repo.insertGpsPoint(it) }

            // Force dwell detection
            repo.detectAndInsertDwell()

            // Insert fake earnings
            val fakeEarnings = listOf(
                Earnings(timestamp = now.minusDays(1), total=120),
                Earnings(timestamp = now.minusDays(2), total=180),
                Earnings(timestamp = now.minusDays(3), total=57),
                Earnings(timestamp = now.minusDays(4), total=300),
            )
            fakeEarnings.forEach { repo.insertEarnings(it) }
        }
    }
}

