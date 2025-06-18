package com.supermassivecode.supervendo.ui

import java.time.LocalDateTime

data class DwellLocationUiModel(
    val locationLabel: String,
    val durationMinutes: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)
