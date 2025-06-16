package com.supermassivecode.supervendo.data.analysis

import com.supermassivecode.supervendo.data.DwellLocationDTO

class MatchingLocationDetector {

    companion object {

        private const val MATCH_DISTANCE_THRESHOLD_METERS = 50.0

        fun locationsCloseEnoughToBeConsideredSame(
            location1: DwellLocationDTO,
            location2: DwellLocationDTO
        ): Boolean {
            val distance = Utils.haversineDistance(
                LatLon(location1.latitude, location1.longitude),
                LatLon(location2.latitude, location2.longitude)
            )
            return distance <= MATCH_DISTANCE_THRESHOLD_METERS
        }
    }
}