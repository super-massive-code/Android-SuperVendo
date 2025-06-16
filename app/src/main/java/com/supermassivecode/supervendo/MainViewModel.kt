package com.supermassivecode.supervendo

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supermassivecode.supervendo.data.TrackingController
import com.supermassivecode.supervendo.data.room.GpsPoint
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = TrackingController(application.applicationContext)

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
}

