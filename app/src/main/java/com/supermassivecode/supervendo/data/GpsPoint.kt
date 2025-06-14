package com.supermassivecode.supervendo.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDateTime

@Entity
data class GpsPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val latitude: Double,
    val longitude: Double,
    val speed: Float
)

@Dao
interface GpsDao {
    @Insert
    suspend fun insert(gps: GpsPoint)

    @Query("SELECT * FROM GpsPoint ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLastPoints(limit: Int): List<GpsPoint>
}