package com.example.a5alaty.di

import android.content.Context
import com.example.a5alaty.database.SalatukDatabase
import com.example.a5alaty.network.ApiService
import com.example.a5alaty.repository.HomeRepository
import com.example.a5alaty.repositoryImp.HomeRepositoryImp
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideHomeRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        fusedLocationProviderClient: FusedLocationProviderClient,
        salatukDataBase: SalatukDatabase
    ): HomeRepository {
        return HomeRepositoryImp(context , apiService , fusedLocationProviderClient , salatukDataBase)
    }
}