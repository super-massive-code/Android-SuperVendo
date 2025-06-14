package com.supermassivecode.supervendo.data

import android.content.Context
import java.time.temporal.ChronoUnit

class TrackerRepo(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val gpsDao = db.gpsDao()
    private val dwellDao = db.dwellDao()
    private val earningsDao = db.earningsDao()

    suspend fun insertGpsPoint(point: GpsPoint) {
        gpsDao.insert(point)
    }

    suspend fun insertDwellLocation(location: DwellLocation) {
        dwellDao.insert(location)
    }

    suspend fun insertEarnings(earnings: Earnings) {
        earningsDao.insert(earnings)
    }

    fun getAllDwellLocations() = dwellDao.getAll()
    fun getAllEarnings() = earningsDao.getAll()

    suspend fun detectAndInsertDwell() {
        val points = gpsDao.getLastPoints(10)
        if (points.size < 5) return

        val isStationary = points.all { it.speed < 1 }

        if (isStationary) {
            val start = points.first()
            val end = points.last()
            val minutesDiff = ChronoUnit.MINUTES.between(start.timestamp, end.timestamp)
            if (minutesDiff >= 10) {
                dwellDao.insert(
                    DwellLocation(
                        startTimestamp = start.timestamp,
                        endTimestamp = end.timestamp,
                        latitude = start.latitude,
                        longitude = end.longitude
                    )
                )
            }
        }
    }
}