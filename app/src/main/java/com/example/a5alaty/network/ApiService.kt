package com.example.a5alaty.network

import com.example.a5alaty.model.remote.Azan
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("timings/{date}")
    suspend fun getPrayerFromApi(
        @Path("date") date: String,
        @Query("latitude") lat: String,
        @Query("longitude") long : String,
        @Query("method") method : Int = 5
    ): Response<Azan>
}