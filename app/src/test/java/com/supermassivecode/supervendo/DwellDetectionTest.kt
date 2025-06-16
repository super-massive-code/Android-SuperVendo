package com.supermassivecode.supervendo

import com.supermassivecode.supervendo.data.analysis.DwellDetection
import com.supermassivecode.supervendo.data.room.GpsPoint
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class DwellDetectionTest {

    private val now = LocalDateTime.now()

    /**
     * True
     */

    @Test
    fun `returns true when all points are slow, duration is at least 10 minutes and movement is tiny`() {
        val points = listOf(
            GpsPoint(latitude = 10.0000, longitude = 20.0000, speed = 0.3f, timestamp = now),
            GpsPoint(latitude = 10.0001, longitude = 20.0000, speed = 0.2f, timestamp = now.plusMinutes(5)),
            GpsPoint(latitude = 10.0001, longitude = 20.0000, speed = 0.4f, timestamp = now.plusMinutes(10))
        )

        assertTrue(DwellDetection.isDwelling(points))
    }

    @Test
    fun `returns true for empty list`() {
        assertTrue(DwellDetection.isDwelling(emptyList()))
    }

    @Test
    fun `returns true when movement is tiny and duration is exactly 10 minutes`() {
        val points = listOf(
            GpsPoint(latitude = 10.0000, longitude = 20.0000, speed = 0.4f, timestamp = now),
            GpsPoint(latitude = 10.0001, longitude = 20.0001, speed = 0.3f, timestamp = now.plusMinutes(10))
        )

        assertTrue(DwellDetection.isDwelling(points))
    }

    /**
     * False
     */

    @Test
    fun `returns false when any speed is too high`() {
        val points = listOf(
            GpsPoint(latitude = 10.0, longitude = 20.0, speed = 0.5f, timestamp = now),
            GpsPoint(latitude = 10.0, longitude = 20.0, speed = 1.5f, timestamp = now.plusMinutes(10))
        )

        assertFalse(DwellDetection.isDwelling(points))
    }

    @Test
    fun `returns false when duration is less than 10 minutes`() {
        val points = listOf(
            GpsPoint(latitude = 10.0, longitude = 20.0, speed = 0.3f, timestamp = now),
            GpsPoint(latitude = 10.0, longitude = 20.0, speed = 0.4f, timestamp = now.plusMinutes(5))
        )

        assertFalse(DwellDetection.isDwelling(points))
    }

    @Test
    fun `returns false when user moved more than 25 meters`() {
        val points = listOf(
            GpsPoint(latitude = 10.0000, longitude = 20.0000, speed = 0.2f, timestamp = now),
            GpsPoint(latitude = 10.0010, longitude = 20.0010, speed = 0.3f, timestamp = now.plusMinutes(10))
        )

        assertFalse(DwellDetection.isDwelling(points))
    }
}
