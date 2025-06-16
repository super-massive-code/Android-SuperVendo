package com.supermassivecode.supervendo.data.room

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import java.time.LocalDateTime

@Entity
data class Day (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val totalEarnings: Long? = null
)

data class DayWithDwellLocations(
    @Embedded val day: Day,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val dwellLocations: List<DwellLocation>
)

@Dao
interface DayDao {
    @Insert
    suspend fun insert(day: Day): Long

    @Query("SELECT * FROM Day ORDER BY timestamp DESC")
    fun getAll(): List<Day>

    @Transaction
    @Query("SELECT * FROM Day WHERE id = :id")
    suspend fun getDayWithDwellLocations(id: Int): DayWithDwellLocations

    @Query("UPDATE Day SET totalEarnings = :total WHERE id = :dayId")
    suspend fun updateTotalEarnings(dayId: Int, total: Long)
}


