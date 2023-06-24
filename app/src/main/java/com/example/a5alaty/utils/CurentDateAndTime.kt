package com.example.a5alaty.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.a5alaty.model.local.NextPrayerTimes
import com.example.a5alaty.model.remote.Timings
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun getSystemDate(): String {
    val currentDate = Calendar.getInstance().time
    val dateFormat =
        SimpleDateFormat("yyyy, MMMM d", Locale.getDefault())
    return dateFormat.format(
        currentDate
    )
}

fun convertDateFormat(dateString: String): String {
    val inputFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy, MMMM d", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return outputFormat.format(date)
}

fun getTime12hrsFormat(time24Format: String): String {
    val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val time = inputFormat.parse(time24Format)
    return outputFormat.format(time!!)
}

//get date for api request
fun getTimeForApi(): String {
    return SimpleDateFormat("d-M-yyyy", Locale.ENGLISH).format(Date())
}

fun getDayCounter(daysToAdd: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)

    val dateFormat = SimpleDateFormat("d-M-yyyy", Locale.ENGLISH)
    val formattedDate = dateFormat.format(calendar.time)
    Log.d("NEXT DAY", formattedDate)

    return formattedDate
}

fun getCurrentTime(): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val currentTime = Calendar.getInstance().time
    return timeFormat.format(currentTime)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNextPrayerTime(prayerTimesList : List<NextPrayerTimes>):NextPrayerTimes{

    val time = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
    val currentTimeValue: LocalTime = LocalTime.parse(getCurrentTime(), time)

    val fajr: LocalTime = LocalTime.parse(prayerTimesList[0].time, time)
    val sunrise: LocalTime = LocalTime.parse(prayerTimesList[1].time, time)
    val dhuhr: LocalTime = LocalTime.parse(prayerTimesList[2].time, time)
    val asr: LocalTime = LocalTime.parse(prayerTimesList[3].time, time)
    val maghrib: LocalTime = LocalTime.parse(prayerTimesList[4].time, time)
    val isha: LocalTime = LocalTime.parse(prayerTimesList[5].time, time)

    return if (currentTimeValue.isBefore(sunrise) && currentTimeValue.isAfter(fajr)) {
        //sunrise
        prayerTimesList[1]
    } else if (currentTimeValue.isBefore(dhuhr) && currentTimeValue.isAfter(sunrise)) {
        //dhuhr
        prayerTimesList[2]
    } else if (currentTimeValue.isBefore(asr) && currentTimeValue.isAfter(dhuhr)) {
        //asr
        prayerTimesList[3]
    } else if (currentTimeValue.isBefore(maghrib) && currentTimeValue.isAfter(asr)) {
        //magribe
        prayerTimesList[4]
    } else if (currentTimeValue.isBefore(isha) && currentTimeValue.isAfter(maghrib)) {
        //isha
        prayerTimesList[5]
    } else {
        //fagr
        prayerTimesList[0]
    }
}

fun currentPrayerTimes() : Timings =  Timings(
    "", "",
    "", "",
    "", "",
    "", "",
    "", "",
    ""
)



