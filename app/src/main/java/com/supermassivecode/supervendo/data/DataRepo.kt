package com.supermassivecode.supervendo.data

import android.content.Context
import com.supermassivecode.supervendo.data.room.AppDatabase
import java.time.LocalDateTime

class DataRepo(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val dayDao = db.dayDao()

    suspend fun getInfoForDay(day: LocalDateTime): DayDTO? {
        val start = day.toLocalDate().atStartOfDay()
        val end = start.plusDays(1)
        dayDao.getDay(start, end)?.let { foundDay ->
            val dayWithDwellLocations = dayDao.getDayWithDwellLocations(foundDay.id)
            return dayWithDwellLocations.toDto()
        } ?: run {
            return null
        }
    }
}