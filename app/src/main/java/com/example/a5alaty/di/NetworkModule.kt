package com.example.a5alaty.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.a5alaty.network.ApiService
import com.example.a5alaty.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(@ApplicationContext appContext: Context) : OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(20 , TimeUnit.SECONDS)
            .readTimeout(20 , TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(appContext))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService{
        return retrofit.create(ApiService::class.java)
    }



}