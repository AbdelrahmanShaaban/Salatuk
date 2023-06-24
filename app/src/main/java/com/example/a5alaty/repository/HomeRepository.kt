package com.example.a5alaty.repository

import android.location.Location
import com.example.a5alaty.model.entities.PrayerTime
import com.example.a5alaty.model.local.NextPrayerTimes
import kotlinx.coroutines.flow.SharedFlow

interface HomeRepository {
    suspend fun getPrayerTimesRemotely(date : String , lat: String, long: String) : PrayerTime
    suspend fun insertPrayerTimes(prayerTime: PrayerTime)
    suspend fun deleteData()
    suspend fun getLatAndLongOfCurrentLocation(): SharedFlow<Location>
    suspend fun getNextAndLastPayerTimes(date: String, lat: String, long: String, ): PrayerTime
    fun getTodayDate() : String
    fun getPrayerTimesLocal(): PrayerTime
    fun getAddressFromLatLong(lat: String, long: String): String
    fun getNextPrayer(nextPrayers: List<NextPrayerTimes>): NextPrayerTimes
}