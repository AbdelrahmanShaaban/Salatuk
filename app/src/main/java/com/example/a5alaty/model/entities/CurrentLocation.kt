package com.example.a5alaty.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CURRENT_LOCATION")
data class CurrentLocation(
    @PrimaryKey(autoGenerate = true)
     val id: Int,
     val lastLocation: String
)
