package com.supermassivecode.supervendo.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDateTime

@Entity
data class DwellLocation (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTimestamp: LocalDateTime,
    val endTimestamp: LocalDateTime,
    val latitude: Double,
    val longitude: Double
)

@Dao
interface DwellDao {
    @Insert
    suspend fun insert(dwell: DwellLocation)

    @Query("SELECT * FROM DwellLocation ORDER BY startTimestamp DESC")
    fun getAll(): List<DwellLocation>
}