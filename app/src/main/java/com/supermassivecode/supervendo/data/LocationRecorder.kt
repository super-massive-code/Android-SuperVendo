package com.supermassivecode.supervendo.data

import android.content.Context
import com.supermassivecode.supervendo.data.analysis.DwellDetection
import com.supermassivecode.supervendo.data.room.AppDatabase
import com.supermassivecode.supervendo.data.room.Day
import com.supermassivecode.supervendo.data.room.DwellLocation

class LocationRecorder(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val gpsDao = db.gpsDao()
    private val dayDao = db.dayDao()
    private val dwellDao = db.dwellDao()

    suspend fun startTracking() {
        //TODO: Implement location tracking logic here, setting up GPS, etc.
    }

    //TODO: acquire GPS and insert every x seconds/minutes
    //TODO: at time interval detect dwell and insert into database

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