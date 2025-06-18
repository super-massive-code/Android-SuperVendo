package com.supermassivecode.supervendo.data

import com.supermassivecode.supervendo.data.room.DayWithDwellLocations
import com.supermassivecode.supervendo.data.room.DwellLocation

fun DayWithDwellLocations.toDto(): DayDTO {
    return DayDTO(
        timestamp = day.timestamp,
        earningsTotal = day.totalEarnings,
        dwellLocations = dwellLocations.map { it.toDto() }
    )
}

fun DwellLocation.toDto(): DwellLocationDTO {
    return DwellLocationDTO(
        latitude = this.latitude,
        longitude = this.longitude,
        startTimestamp = this.startTimestamp,
        endTimestamp = this.endTimestamp
    )
}
