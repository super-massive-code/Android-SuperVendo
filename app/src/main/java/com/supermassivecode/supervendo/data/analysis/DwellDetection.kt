package com.supermassivecode.supervendo.data.analysis

import com.supermassivecode.supervendo.data.room.GpsPoint
import java.time.Duration

class DwellDetection {

    companion object {

        private const val SPEED_THRESHOLD_MPS = 1.0
        private const val DWELL_RADIUS_METERS = 25.0
        private const val DWELL_TIME_MINUTES = 10L

        fun isDwelling(points: List<GpsPoint>): Boolean {
            if (points.size < 2) return true

            val durationMinutes = Duration.between(
                points.minOf { it.timestamp },
                points.maxOf { it.timestamp }
            ).toMinutes()

            if (durationMinutes < DWELL_TIME_MINUTES) return false

            val allSpeedsLow = points.all { it.speed < SPEED_THRESHOLD_MPS }
            if (!allSpeedsLow) return false

            val centerLat = points.map { it.latitude }.average()
            val centerLon = points.map { it.longitude }.average()
            val centerPoint = LatLon(latitude = centerLat, longitude = centerLon)

            val maxDistanceFromCenter = points.map { LatLon(it.latitude, it.longitude) }.maxOf { Utils.haversineDistance(it, centerPoint) }

            return maxDistanceFromCenter < DWELL_RADIUS_METERS
        }
    }
}