package com.supermassivecode.supervendo.data.analysis

import com.supermassivecode.supervendo.data.DayDTO
import com.supermassivecode.supervendo.data.DwellLocationDTO

data class RecommendedLocation(
    val latitude: Double,
    val longitude: Double,
    val totalEarnings: Long,
    val visitCount: Int,
    val averageEarnings: Long
)

/**
 * Analyzes a list of DayDTO objects and recommends top locations based on historical dwell data and earnings.
 */

class LocationRecommender {

    private data class LocationGroup(
        val members: MutableList<Pair<DwellLocationDTO, Long>>,
        var centerLat: Double,
        var centerLon: Double
    ) {
        fun updateCenter() {
            centerLat = members.map { it.first.latitude }.average()
            centerLon = members.map { it.first.longitude }.average()
        }
    }

    fun recommendTopLocations(days: List<DayDTO>): List<RecommendedLocation> {
        val locationGroups = mutableListOf<LocationGroup>()

        for (day in days) {
            if (day.earningsTotal == null) {
                continue
            }

            val earningsPerLocation = day.earningsTotal / maxOf(day.dwellLocations.size, 1)

            for (dwell in day.dwellLocations) {
                val matchingGroup = locationGroups.firstOrNull { group ->
                    MatchingLocationDetector.locationsCloseEnoughToBeConsideredSame(
                        dwell,
                        DwellLocationDTO(
                            startTimestamp = dwell.startTimestamp,
                            endTimestamp = dwell.endTimestamp,
                            latitude = group.centerLat,
                            longitude = group.centerLon
                        )
                    )
                }

                if (matchingGroup != null) {
                    matchingGroup.members.add(dwell to earningsPerLocation)
                    matchingGroup.updateCenter()
                } else {
                    locationGroups.add(
                        LocationGroup(
                            members = mutableListOf(dwell to earningsPerLocation),
                            centerLat = dwell.latitude,
                            centerLon = dwell.longitude
                        )
                    )
                }
            }
        }

        return locationGroups.map { group ->
            val latitudes = group.members.map { it.first.latitude }
            val longitudes = group.members.map { it.first.longitude }
            val earnings = group.members.map { it.second }

            RecommendedLocation(
                latitude = latitudes.average(),
                longitude = longitudes.average(),
                totalEarnings = earnings.sum(),
                visitCount = earnings.size,
                averageEarnings = earnings.sum() / earnings.size
            )
        }
            .filter { it.totalEarnings > 0 }
            .sortedByDescending { it.averageEarnings }
    }
}