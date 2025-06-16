package com.supermassivecode.supervendo.data.analysis

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class LatLon(val latitude: Double, val longitude: Double)

class Utils {

    companion object {

        private const val EARTH_RADIUS_METERS = 6371000.0

        fun haversineDistance(p1: LatLon, p2: LatLon): Double {
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