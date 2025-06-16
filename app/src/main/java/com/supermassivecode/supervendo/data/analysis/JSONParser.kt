package com.supermassivecode.supervendo.data.analysis

import android.content.Context
import com.supermassivecode.supervendo.data.DayDTO
import com.supermassivecode.supervendo.data.DwellLocationDTO
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
private data class JSONDay(
    val date: String,
    val earningsTotal: Long,
    val dwellLocations: List<JSONDwellLocation>
)

@Serializable
private data class JSONDwellLocation(
    val startTimestamp: String,
    val endTimestamp: String,
    val latitude: Double,
    val longitude: Double
)

/**
 * JSONParser is a utility class responsible for parsing structured JSON files into application-specific data models.
 *
 * It reads a JSON file from the app's assets directory and converts its contents into a list of DayDTO objects,
 * each representing a day of tracked data including total earnings and dwell location details.
 *
 * The JSON file is expected to contain a list of day entries, where each entry includes:
 * - A date string (ISO_LOCAL_DATE_TIME format)
 * - A total earnings value
 * - A list of dwell locations, each with start and end timestamps and geographic coordinates
 *
 * Key responsibilities:
 * - Deserialize the JSON structure into internal data representations (`JSONDay`, `JSONDwellLocation`)
 * - Convert these internal models into the app’s domain models (`DayDTO`, `DwellLocationDTO`)
 * - Handle proper timestamp parsing using ISO format
 *
 * Usage:
 * JSONParser.convertToDays(context, "data.json") returns a list of DayDTO from the asset file.
 */

class JSONParser {

    companion object {

        fun convertToDays(context: Context, filename: String): List<DayDTO> {
            val jsonStr = context.assets.open(filename).bufferedReader().use { it.readText() }
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val items = Json.decodeFromString<List<JSONDay>>(jsonStr)
            return items.map { day ->
                DayDTO(
                    timestamp = LocalDateTime.parse(day.date, formatter),
                    earningsTotal = day.earningsTotal,
                    dwellLocations = day.dwellLocations.map { location ->
                        DwellLocationDTO(
                            startTimestamp = LocalDateTime.parse(
                                location.startTimestamp,
                                formatter
                            ),
                            endTimestamp = LocalDateTime.parse(location.endTimestamp, formatter),
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    }
                )
            }
        }
    }
}