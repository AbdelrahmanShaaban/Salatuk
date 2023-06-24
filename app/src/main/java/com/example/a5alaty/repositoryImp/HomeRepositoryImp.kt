package com.example.a5alaty.repositoryImp

import android.content.Context
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.a5alaty.database.SalatukDatabase
import com.example.a5alaty.model.entities.PrayerTime
import com.example.a5alaty.model.local.NextPrayerTimes
import com.example.a5alaty.network.ApiService
import com.example.a5alaty.repository.HomeRepository
import com.example.a5alaty.utils.getAddressOfCurrentLocation
import com.example.a5alaty.utils.getCurrentLocation
import com.example.a5alaty.utils.getNextPrayerTime
import com.example.a5alaty.utils.getTime12hrsFormat
import com.example.a5alaty.utils.getTimeForApi
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private var fusedLocationProviderClient: FusedLocationProviderClient,
    private val salatukDataBase: SalatukDatabase
) : HomeRepository {
    private var currentPrayerTime = PrayerTime(0, "", "", "", "", "", "", "")

    override suspend fun getPrayerTimesRemotely(
        date: String,
        lat: String,
        long: String
    ): PrayerTime {
        val prayerTimeFromApi = apiService.getPrayerFromApi(date, lat, long)
        val prayerTimesResponse = prayerTimeFromApi.body()?.data?.timings

        if (prayerTimeFromApi.isSuccessful) {
            currentPrayerTime.fajr = getTime12hrsFormat(prayerTimesResponse?.Fajr.toString())
            currentPrayerTime.sunrise = getTime12hrsFormat(prayerTimesResponse?.Sunrise.toString())
            currentPrayerTime.dhuhr = getTime12hrsFormat(prayerTimesResponse?.Dhuhr.toString())
            currentPrayerTime.asr = getTime12hrsFormat(prayerTimesResponse?.Asr.toString())
            currentPrayerTime.maghrib = getTime12hrsFormat(prayerTimesResponse?.Maghrib.toString())
            currentPrayerTime.isha = getTime12hrsFormat(prayerTimesResponse?.Isha.toString())

            //deleteData()
            insertPrayerTimes(currentPrayerTime)
            return currentPrayerTime
        }else {
            //getDataBaseData

            currentPrayerTime = getPrayerTimesLocal()

            return currentPrayerTime
        }
    }

    override fun getPrayerTimesLocal(): PrayerTime {
        return salatukDataBase.prayerTimeDao().getDayTimings()
    }

    override suspend fun insertPrayerTimes(prayerTime: PrayerTime) {
        salatukDataBase.prayerTimeDao().insertPrayers(prayerTime)
    }

    override suspend fun deleteData() {
        salatukDataBase.prayerTimeDao().deleteOldData()
    }

    override fun getAddressFromLatLong(lat: String, long: String): String {
        return getAddressOfCurrentLocation(context , lat.toDouble() , long.toDouble())!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getNextPrayer(nextPrayers: List<NextPrayerTimes>): NextPrayerTimes {
        return getNextPrayerTime(nextPrayers)
    }

    override suspend fun getLatAndLongOfCurrentLocation(): SharedFlow<Location> {
        return getCurrentLocation(fusedLocationProviderClient)
    }

    override suspend fun getNextAndLastPayerTimes(
        date: String,
        lat: String,
        long: String
    ): PrayerTime {
        val prayerTimeFromApi = apiService.getPrayerFromApi(date, lat, long)
        val prayerTimesResponse = prayerTimeFromApi.body()?.data?.timings

        if (prayerTimeFromApi.isSuccessful) {
            currentPrayerTime.fajr = getTime12hrsFormat(prayerTimesResponse?.Fajr.toString())
            currentPrayerTime.sunrise = getTime12hrsFormat(prayerTimesResponse?.Sunrise.toString())
            currentPrayerTime.dhuhr = getTime12hrsFormat(prayerTimesResponse?.Dhuhr.toString())
            currentPrayerTime.asr = getTime12hrsFormat(prayerTimesResponse?.Asr.toString())
            currentPrayerTime.maghrib = getTime12hrsFormat(prayerTimesResponse?.Maghrib.toString())
            currentPrayerTime.isha = getTime12hrsFormat(prayerTimesResponse?.Isha.toString())

            //deleteData()
            insertPrayerTimes(currentPrayerTime)
            return currentPrayerTime
        }else {
            //getDataBaseData

            currentPrayerTime = getPrayerTimesLocal()

            return currentPrayerTime
        }
    }

    override fun getTodayDate(): String {
        return getTimeForApi()
    }


}