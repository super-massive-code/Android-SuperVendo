package com.supermassivecode.supervendo

import com.supermassivecode.supervendo.data.DayDTO
import com.supermassivecode.supervendo.data.DwellLocationDTO
import com.supermassivecode.supervendo.data.analysis.LocationRecommender
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class LocationRecommenderTest {

    private val now = LocalDateTime.now()
    private val recommender = LocationRecommender()

    @Test
    fun `locations within 50 meters are grouped together`() {
        val location1 = DwellLocationDTO(now, now, 10.000000, 20.000000)
        val location2 = DwellLocationDTO(now, now, 10.000300, 20.000300) // within 50m
        val day = DayDTO(
            timestamp = now,
            earningsTotal = 200,
            dwellLocations = listOf(location1, location2)
        )

        val result = recommender.recommendTopLocations(listOf(day))

        assertEquals(1, result.size)
        assertEquals(200, result.first().totalEarnings)
        assertEquals(2, result.first().visitCount)
        assertEquals(100, result.first().averageEarnings)
    }

    @Test
    fun `locations farther than 50 meters are separated into different groups`() {
        val location1 = DwellLocationDTO(now, now, 10.000000, 20.000000)
        val location2 = DwellLocationDTO(now, now, 10.010000, 20.010000) // ~1km away
        val day = DayDTO(
            timestamp = now,
            earningsTotal = 200,
            dwellLocations = listOf(location1, location2)
        )

        val result = recommender.recommendTopLocations(listOf(day))

        assertEquals(2, result.size)
        assertTrue(result.all { it.visitCount == 1 })
        assertTrue(result.all { it.totalEarnings == 100L })
    }

    @Test
    fun `group centers are updated correctly when new dwell is added`() {
        val nearLocation1 = DwellLocationDTO(now, now, 10.000000, 20.000000)
        val nearLocation2 = DwellLocationDTO(now, now, 10.000200, 20.000200) // slightly offset
        val day = DayDTO(
            timestamp = now,
            earningsTotal = 200,
            dwellLocations = listOf(nearLocation1, nearLocation2)
        )

        val result = recommender.recommendTopLocations(listOf(day))

        val group = result.first()
        assertEquals(2, group.visitCount)
        assertEquals(100, group.averageEarnings)

        // Center should be roughly between the two points
        assertTrue(group.latitude in 10.0000..10.0002)
        assertTrue(group.longitude in 20.0000..20.0002)
    }

    @Test
    fun `results are sorted by average earnings descending`() {
        val richLocation = DwellLocationDTO(now, now, 10.0000, 20.0000)
        val poorLocation = DwellLocationDTO(now, now, 11.0000, 21.0000)

        val days = listOf(
            DayDTO(
                timestamp = now,
                earningsTotal = 1000,
                dwellLocations = listOf(richLocation)
            ),
            DayDTO(
                timestamp = now.plusDays(1),
                earningsTotal = 100,
                dwellLocations = listOf(poorLocation)
            )
        )

        val result = recommender.recommendTopLocations(days)

        assertEquals(2, result.size)
        assertTrue(result[0].averageEarnings > result[1].averageEarnings)
    }

    @Test
    fun `empty dwell location list returns no recommendations`() {
        val day = DayDTO(
            timestamp = now,
            earningsTotal = 100,
            dwellLocations = emptyList()
        )

        val result = recommender.recommendTopLocations(listOf(day))

        assertTrue(result.isEmpty())
    }


    @Test
    fun `zero earnings is excluded from recommendations`() {
        val dwell = DwellLocationDTO(now, now, 10.0, 20.0)
        val day = DayDTO(now, 0, listOf(dwell))

        val result = recommender.recommendTopLocations(listOf(day))
        assertEquals(0, result.size)
    }
}
