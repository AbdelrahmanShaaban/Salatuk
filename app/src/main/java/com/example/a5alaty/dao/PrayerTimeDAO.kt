package com.example.a5alaty.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.a5alaty.model.entities.PrayerTime

@Dao
interface PrayerTimeDAO {

    @Insert
    suspend fun insertPrayers(time: PrayerTime)

    @Update
    suspend fun updateTimings(time: PrayerTime)

    @Query("SELECT*FROM `PRAYER_TIME` ORDER BY id DESC LIMIT 1")
    fun getDayTimings(): PrayerTime

    @Query("SELECT COUNT(*) FROM `PRAYER_TIME`")
    fun isTableEmpty(): Int
    @Query("DELETE FROM `PRAYER_TIME`")
    suspend fun deleteOldData()
}