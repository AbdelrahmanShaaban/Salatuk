package com.example.a5alaty.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a5alaty.model.entities.PrayerTime
import com.example.a5alaty.model.local.NextPrayerTimes
import com.example.a5alaty.model.remote.Azan
import com.example.a5alaty.model.remote.Timings
import com.example.a5alaty.repository.HomeRepository
import com.example.a5alaty.repositoryImp.HomeRepositoryImp
import com.example.a5alaty.utils.currentPrayerTimes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _myAddressFlow: MutableStateFlow<String> = MutableStateFlow("")
    val myAddressFlow : StateFlow<String> = _myAddressFlow

    private val _myLocationFlow: MutableStateFlow<Location?> = MutableStateFlow(Location(""))
    val myLocationFlow: StateFlow<Location?> = _myLocationFlow

    private val _prayerTimesFlow : MutableStateFlow<Timings> = MutableStateFlow(currentPrayerTimes())
    val prayerTimesFlow : StateFlow<Timings> = _prayerTimesFlow

    private val _nextPrayerName: MutableStateFlow<NextPrayerTimes> =
        MutableStateFlow(NextPrayerTimes("", ""))
    val nextPrayerName: StateFlow<NextPrayerTimes> = _nextPrayerName

    fun getLatAndLongCurrent(){
        viewModelScope.launch {
            repository.getLatAndLongOfCurrentLocation().collect{
                _myLocationFlow.emit(it)
            }
        }
    }

    fun getPrayerTimesRemotely(
        date: String,
        lat: String,
        long: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val prayerTimesResponse = repository.getPrayerTimesRemotely(date , lat, long)
            _prayerTimesFlow.emit(
                Timings(
                    prayerTimesResponse.asr,
                    prayerTimesResponse.dhuhr,
                    prayerTimesResponse.fajr,
                    "",
                    "",
                    prayerTimesResponse.isha,
                    "",
                    prayerTimesResponse.maghrib,
                    "",
                    prayerTimesResponse.sunrise,
                    prayerTimesResponse.sunset
                )
            )
        }

    }


    fun getPrayerTimeslocal(){
        viewModelScope.launch(Dispatchers.IO) {
            val prayerTimesResponse = repository.getPrayerTimesLocal()
            _prayerTimesFlow.emit(
                Timings(
                    prayerTimesResponse.asr,
                    prayerTimesResponse.dhuhr,
                    prayerTimesResponse.fajr,
                    "",
                    "",
                    prayerTimesResponse.isha,
                    "",
                    prayerTimesResponse.maghrib,
                    "",
                    prayerTimesResponse.sunrise,
                    prayerTimesResponse.sunset
                )
            )
        }

    }
    fun getMyAddress(latitude: String, longitude: String) : String{
        val myAddress = repository.getAddressFromLatLong(latitude , longitude)
        viewModelScope.launch {
            _myAddressFlow.emit(myAddress)
        }
        return myAddress

    }

    fun getTodayDate() : String{
        return repository.getTodayDate()
    }

    fun getNextPrayer(prayers: List<NextPrayerTimes>) {
        viewModelScope.launch {
            _nextPrayerName.emit(repository.getNextPrayer(prayers))
        }
    }

    fun getNextAndLastPrayerTimesRemotely(
        date: String,
        lat: String,
        long: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val prayerTimesResponse = repository.getPrayerTimesRemotely(date , lat, long)
            _prayerTimesFlow.emit(
                Timings(
                    prayerTimesResponse.asr,
                    prayerTimesResponse.dhuhr,
                    prayerTimesResponse.fajr,
                    "",
                    "",
                    prayerTimesResponse.isha,
                    "",
                    prayerTimesResponse.maghrib,
                    "",
                    prayerTimesResponse.sunrise,
                    prayerTimesResponse.sunset
                )
            )
        }

    }

}