package com.example.a5alaty.model.remote

data class Date(
    val gregorian: Gregorian?,
    val hijri: Hijri?,
    val readable: String?,
    val timestamp: String?
)