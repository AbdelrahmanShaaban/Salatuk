package com.example.a5alaty.model.remote

data class Timings(
    var Asr: String?,
    var Dhuhr: String?,
    var Fajr: String?,
    val Firstthird: String?,
    val Imsak: String?,
    var Isha: String?,
    val Lastthird: String?,
    var Maghrib: String?,
    val Midnight: String?,
    var Sunrise: String?,
    var Sunset: String?
)