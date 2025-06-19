package com.supermassivecode.supervendo.ui.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.supermassivecode.supervendo.data.DataRepo
import com.supermassivecode.supervendo.data.room.DatabaseSeeder
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = DataRepo(application)

    private val _dwellLocations = MutableLiveData<List<DwellLocationUiModel>>()
    val dwellLocations: LiveData<List<DwellLocationUiModel>> = _dwellLocations

    private val _earningsToday = MutableLiveData<String>()
    val earningsToday: LiveData<String> = _earningsToday

    private val _todayDate = MutableLiveData<String>()
    val todayDate: LiveData<String> = _todayDate

    init {
        loadTodaySummary()
    }

    @SuppressLint("DefaultLocale")
    private fun loadTodaySummary() {
        viewModelScope.launch {
            DatabaseSeeder(application.applicationContext).seedDatabase()
            repo.getInfoForDay(LocalDateTime.now())?.let { day ->
                val uiList = day.dwellLocations.map { location ->
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
                _earningsToday.postValue("${day.earningsTotal}")
                _dwellLocations.postValue(uiList)
            }
        }
    }

    private fun formatLatLng(lat: Double, lng: Double): String {
        return "%.5f, %.5f".format(lat, lng)
    }
}
