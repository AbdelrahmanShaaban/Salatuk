package com.example.a5alaty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a5alaty.dao.PrayerTimeDAO
import com.example.a5alaty.model.entities.PrayerTime

@Database(entities = [PrayerTime::class], version = 1, exportSchema = false)
abstract class SalatukDatabase : RoomDatabase(){
    abstract fun prayerTimeDao(): PrayerTimeDAO
}