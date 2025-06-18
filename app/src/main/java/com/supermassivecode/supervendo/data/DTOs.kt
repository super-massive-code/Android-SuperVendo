package com.supermassivecode.supervendo.data

import java.time.LocalDateTime

data class DayDTO(
    val timestamp: LocalDateTime,
    val earningsTotal: Long?,
    val dwellLocations: List<DwellLocationDTO>
)

data class DwellLocationDTO(
    val startTimestamp: LocalDateTime,
    val endTimestamp: LocalDateTime,
    val latitude: Double,
    val longitude: Double
)