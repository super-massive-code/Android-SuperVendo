package com.supermassivecode.supervendo.data

import java.time.Duration
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class DwellDetection {

    companion object {

        private const val SPEED_THRESHOLD_MPS = 1.0
        private const val DWELL_RADIUS_METERS = 25.0
        private const val DWELL_TIME_MINUTES = 10L
        private const val EARTH_RADIUS_METERS = 6371000.0

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
            val centerPoint = GpsPoint(latitude = centerLat, longitude = centerLon, speed = 0.0f)

            val maxDistanceFromCenter = points.maxOf { haversine(it, centerPoint) }

            return maxDistanceFromCenter < DWELL_RADIUS_METERS
        }

        private fun haversine(p1: GpsPoint, p2: GpsPoint): Double {
            val lat1 = Math.toRadians(p1.latitude)
            val lat2 = Math.toRadians(p2.latitude)
            val deltaLat = Math.toRadians(p2.latitude - p1.latitude)
            val deltaLon = Math.toRadians(p2.longitude - p1.longitude)

            val a = sin(deltaLat / 2).pow(2) +
                    cos(lat1) * cos(lat2) *
                    sin(deltaLon / 2).pow(2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return EARTH_RADIUS_METERS * c
        }
    }
}