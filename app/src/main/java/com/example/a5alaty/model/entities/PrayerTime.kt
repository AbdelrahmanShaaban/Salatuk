package com.example.a5alaty.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PRAYER_TIME")
data class PrayerTime(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var fajr: String,
    var sunrise: String,
    var dhuhr: String,
    var asr: String,
    var maghrib: String,
    var sunset: String,
    var isha: String,
)
