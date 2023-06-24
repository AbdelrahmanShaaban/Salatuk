package com.example.a5alaty.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.example.a5alaty.utils.Constants.CAIRO_LAT
import com.example.a5alaty.utils.Constants.CAIRO_LONG
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient): SharedFlow<Location> {

    val locationMutableLiveData = MutableSharedFlow<Location>()
    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
        GlobalScope.launch {
            locationMutableLiveData.emit(it)
        }
    }.addOnFailureListener {
        val lastLocation = Location("")
        lastLocation.latitude = CAIRO_LAT
        lastLocation.longitude = CAIRO_LONG
        GlobalScope.launch {
            locationMutableLiveData.emit(lastLocation)
        }
    }
    return locationMutableLiveData
}

@Suppress("DEPRECATION")
fun getAddressOfCurrentLocation(context: Context, lat: Double, long: Double): String? {

    val geocoder = Geocoder(context, Locale.getDefault())
    val address: Address?
    val addressGeocoder = geocoder.getFromLocation(lat, long, 1)

    if (addressGeocoder != null) {
        address = addressGeocoder[0]
        val city = address?.locality
        val state = address?.adminArea
        val country = address?.countryName
        return "$city, $state, $country"
    }
    return null
}