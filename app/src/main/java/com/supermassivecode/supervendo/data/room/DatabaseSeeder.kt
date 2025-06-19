package com.supermassivecode.supervendo.data.room


import android.content.Context
import com.supermassivecode.supervendo.data.DayDataReader
import java.time.LocalDateTime

class DatabaseSeeder(context: Context) {

    private val repo = DayDataReader(context)
    private val db = AppDatabase.getInstance(context)
    private val gpsDao = db.gpsDao()
    private val dwellDao = db.dwellDao()
    private val dayDao = db.dayDao()

    suspend fun seedDatabase() {
        // Seed if Day table is empty
        val now = LocalDateTime.now()

        // 1) Day
        val dayEntity = Day(
            timestamp     = now.toLocalDate().atStartOfDay(),
            totalEarnings = 150L
        )
        val dayId = dayDao.insert(dayEntity)

        // 2) DwellLocations
        val dwells = listOf(
            DwellLocation(
                dayId          = dayId.toInt(),
                latitude       = 37.7749,
                longitude      = -122.4194,
                startTimestamp = now.minusHours(3),
                endTimestamp   = now.minusHours(2)
            ),
            DwellLocation(
                dayId          = dayId.toInt(),
                latitude       = 37.7750,
                longitude      = -122.4195,
                startTimestamp = now.minusHours(1),
                endTimestamp   = now
            )
        )
        dwells.forEach { dwellDao.insert(it) }
    }
}
