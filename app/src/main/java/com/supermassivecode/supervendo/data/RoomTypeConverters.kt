package com.supermassivecode.supervendo.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class RoomTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime =
        LocalDateTime.ofEpochSecond(value / 1000, 0, ZoneOffset.UTC)

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime): Long =
        date.toEpochSecond(ZoneOffset.UTC) * 1000
}
