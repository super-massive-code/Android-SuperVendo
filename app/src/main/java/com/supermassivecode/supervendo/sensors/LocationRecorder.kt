package com.supermassivecode.supervendo.sensors

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.supermassivecode.supervendo.data.DayDataReader
import com.supermassivecode.supervendo.data.analysis.DwellDetection
import com.supermassivecode.supervendo.data.room.AppDatabase
import com.supermassivecode.supervendo.data.room.DwellLocation
import com.supermassivecode.supervendo.data.room.GpsPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class LocationRecorder(context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val db = AppDatabase.getInstance(context)
    private val gpsDao = db.gpsDao()
    private val dwellDao = db.dwellDao()

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5 * 60 * 1000L // 5 minutes
    ).build()

    private var isTracking = false

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { location ->
                val gpsPoint = GpsPoint(
                    timestamp = LocalDateTime.now(),
                    latitude = location.latitude,
                    longitude = location.longitude,
                    speed = location.speed
                )

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("LocationRecorder", "Inserting location: $gpsPoint")
                    gpsDao.insert(gpsPoint)
                    if (DwellDetection.isDwelling(gpsDao.getLastPoints(10))) {
                        val dayId = DayDataReader(context).getCurrentDayId()
                        val dwellLocation = DwellLocation(
                            dayId = dayId,
                            startTimestamp = gpsPoint.timestamp,
                            endTimestamp = gpsPoint.timestamp,
                            latitude = gpsPoint.latitude,
                            longitude = gpsPoint.longitude
                        )
                        Log.d("LocationRecorder", "Inserting Dwell $dwellLocation")
                        dwellDao.insert(dwellLocation)
                    }
                }
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            Log.d("LocationRecorder", "Location available: ${availability.isLocationAvailable}")
        }
    }

    fun startTracking() {
        if (isTracking) return
        isTracking = true

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Log.d("LocationRecorder", "Started location tracking")
        } catch (e: SecurityException) {
            Log.e("LocationRecorder", "Missing location permission", e)
        }
    }

    fun stopTracking() {
        if (!isTracking) return
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isTracking = false
        Log.d("LocationRecorder", "Stopped location tracking")
    }

    fun getLastKnownLocation(onResult: (Location?) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                onResult(location)
            }.addOnFailureListener {
                Log.e("LocationRecorder", "Failed to get last known location", it)
                onResult(null)
            }
        } catch (e: SecurityException) {
            Log.e("LocationRecorder", "Missing location permission", e)
            onResult(null)
        }
    }
}
