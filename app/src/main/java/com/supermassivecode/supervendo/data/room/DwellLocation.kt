package com.supermassivecode.supervendo.data.room

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Day::class,
        parentColumns = ["id"],
        childColumns = ["dayId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("dayId")]
)
data class DwellLocation (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayId: Int,
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