package com.supermassivecode.supervendo.data

import android.content.Context
import com.supermassivecode.supervendo.data.room.AppDatabase
import com.supermassivecode.supervendo.data.room.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class DayDataReader(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val dayDao = db.dayDao()

    /**
     * Returns today's Day record from the database if it exists.
     * Otherwise, inserts a new one for today starting at midnight.
     */
    suspend fun getOrCreateToday(): Day {
        val now = LocalDateTime.now()
        val start = now.toLocalDate().atStartOfDay()
        val end = start.plusDays(1)

        val existingDay = dayDao.getDay(start, end)
        if (existingDay != null) return existingDay

        val newDay = Day(timestamp = start, totalEarnings = 0L)
        val id = dayDao.insert(newDay).toInt()
        return newDay.copy(id = id)
    }

    /**
     * Returns the ID of today's Day object (creating it if needed).
     */
    suspend fun getCurrentDayId(): Int {
        return getOrCreateToday().id
    }

    /**
     * Returns a Flow that emits updates to today's Day (with dwell locations),
     * creating the Day record if it doesn't exist.
     */
    suspend fun getCurrentDayFlow(): Flow<DayDTO?> {
        val day = getOrCreateToday()
        return dayDao.getDayWithDwellLocationsAsFlow(day.id)
            .map { it.toDto() }
    }
}

