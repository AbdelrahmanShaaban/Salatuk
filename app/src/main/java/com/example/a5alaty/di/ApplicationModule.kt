package com.example.a5alaty.di

import android.content.Context
import androidx.room.Room
import com.example.a5alaty.database.SalatukDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }

    @Provides
    fun provideSalatukDataBase(@ApplicationContext context: Context): SalatukDatabase {
        return Room.databaseBuilder(context, SalatukDatabase::class.java,"PRAYER_TIMES_DATABASE").build()
    }
}