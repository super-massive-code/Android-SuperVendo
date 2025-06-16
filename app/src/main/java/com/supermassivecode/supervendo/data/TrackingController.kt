package com.supermassivecode.supervendo.data

import android.content.Context
import com.supermassivecode.supervendo.data.analysis.DwellDetection
import com.supermassivecode.supervendo.data.room.AppDatabase
import com.supermassivecode.supervendo.data.room.DwellLocation
import com.supermassivecode.supervendo.data.room.Day
import com.supermassivecode.supervendo.data.room.GpsPoint

class TrackingController(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val gpsDao = db.gpsDao()
    private val dwellDao = db.dwellDao()
    private val dayDao = db.dayDao()

    suspend fun insertGpsPoint(point: GpsPoint) {
        gpsDao.insert(point)
    }

    suspend fun insertDwellLocation(location: DwellLocation) {
        dwellDao.insert(location)
    }

    suspend fun insertEarnings(day: Day) {
        dayDao.insert(day)
    }

    fun getAllDwellLocations() = dwellDao.getAll()
    fun getAllDays() = dayDao.getAll()

    suspend fun detectAndInsertDwell() {
        val dayId = dayDao.insert(Day())
        val points = gpsDao.getLastPoints(10)
        if (DwellDetection.isDwelling(points)) {
            val start = points.first()
            val end = points.last()
            dwellDao.insert(
                DwellLocation(
                    dayId = dayId.toInt(),
                    startTimestamp = start.timestamp,
                    endTimestamp = end.timestamp,
                    latitude = start.latitude,
                    longitude = end.longitude
                )
            )
        }
    }
}