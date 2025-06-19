package com.supermassivecode.supervendo.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.supermassivecode.supervendo.data.DayDataReader
import com.supermassivecode.supervendo.data.room.DatabaseSeeder
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = DayDataReader(application)

    private val _dwellLocations = MutableLiveData<List<DwellLocationUiModel>>()
    val dwellLocations: LiveData<List<DwellLocationUiModel>> = _dwellLocations

    private val _earningsToday = MutableLiveData<String>()
    val earningsToday: LiveData<String> = _earningsToday

    private val _todayDate = MutableLiveData<String>()
    val todayDate: LiveData<String> = _todayDate

    init {
        observeTodaySummary()
    }

    private fun observeTodaySummary() {
        viewModelScope.launch {
            DatabaseSeeder(getApplication()).seedDatabase()
            //TODO: how would we handle overlap of midnight... we would need to re-trigger this
            repo.getCurrentDayFlow().collect { day ->
                day?.let {
                    val uiList = it.dwellLocations.map { location ->
                        val duration = Duration.between(location.startTimestamp, location.endTimestamp).toMinutes()
                        DwellLocationUiModel(
                            locationLabel = formatLatLng(location.latitude, location.longitude),
                            durationMinutes = duration,
                            startTime = location.startTimestamp,
                            endTime = location.endTimestamp
                        )
                    }

                    _todayDate.postValue(
                        LocalDate
                            .now()
                            .format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
                    )
                    _earningsToday.postValue("${it.earningsTotal}")
                    _dwellLocations.postValue(uiList)
                }
            }
        }
    }

    private fun formatLatLng(lat: Double, lng: Double): String {
        return "%.5f, %.5f".format(lat, lng)
    }
}
