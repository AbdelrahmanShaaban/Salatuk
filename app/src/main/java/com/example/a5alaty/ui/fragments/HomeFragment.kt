package com.example.a5alaty.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.a5alaty.databinding.FragmentHomeBinding
import com.example.a5alaty.model.local.NextPrayerTimes
import com.example.a5alaty.model.remote.Timings
import com.example.a5alaty.utils.convertDateFormat
import com.example.a5alaty.utils.getDayCounter
import com.example.a5alaty.utils.getSystemDate
import com.example.a5alaty.utils.isInternetConnected
import com.example.a5alaty.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val currentLocation = Location("")
    private var myAddress = ""
    private var dayCounter = 0
    private val currentTimings = Timings("", "", "", "", "", "", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        viewModel.getLatAndLongCurrent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons(view)

        lifecycleScope.launch {
            viewModel.myLocationFlow.collect {
                currentLocation.latitude = it!!.latitude
                currentLocation.longitude = it.longitude
                currentLocation.altitude = it.altitude
            }
        }
        if (isInternetConnected(requireContext())) {
            lifecycleScope.launch {

                delay(2000)
                viewModel.getPrayerTimesRemotely(
                    viewModel.getTodayDate(),
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString()
                )
                myAddress = viewModel.getMyAddress(
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString()
                )
            }
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.getPrayerTimeslocal()
            }
        }

        lifecycleScope.launch {
            viewModel.prayerTimesFlow.collect {
                currentTimings.Fajr = it.Fajr
                currentTimings.Sunrise = it.Sunrise
                currentTimings.Dhuhr = it.Dhuhr
                currentTimings.Asr = it.Asr
                currentTimings.Maghrib = it.Maghrib
                currentTimings.Isha = it.Isha
                getPrayerTimes(currentTimings)

            }
        }

        lifecycleScope.launch {
            viewModel.myAddressFlow.collect {
                myAddress = it
            }
        }

        lifecycleScope.launch {
            delay(3000)
            getNextPrayer(currentTimings)

        }
    }

    private fun getPrayerTimes(timings: Timings) {
        binding.myLocationTV.text = myAddress
        binding.degreeSunriseCardTV.text = timings.Sunrise
        binding.timeFajrCardTV.text = timings.Fajr
        binding.timeDuhrCardTV.text = timings.Dhuhr
        binding.timeAsrCardTV.text = timings.Asr
        binding.timeMaghribCardTV.text = timings.Maghrib
        binding.timeishaCardTV.text = timings.Isha
        binding.degreeSunsetCardTV.text = timings.Maghrib

    }

    private  fun getNextPrayer(timings: Timings) {
        val prayerTimes = listOf(
            NextPrayerTimes("Fagr", timings.Fajr.toString()),
            NextPrayerTimes("Sunrise", timings.Sunrise.toString()),
            NextPrayerTimes("Duhr", timings.Dhuhr.toString()),
            NextPrayerTimes("Asr", timings.Asr.toString()),
            NextPrayerTimes("Maghrib", timings.Maghrib.toString()),
            NextPrayerTimes("Isha", timings.Isha.toString()),
        )
        viewModel.getNextPrayer(prayerTimes)


    }

    private fun getDayTimings(counterType: String) {
        if (counterType == "--") {
            dayCounter--
        }
        if (counterType == "++") {
            dayCounter++
        }
        if (isInternetConnected(requireContext())) {
            lifecycleScope.launch {
                viewModel.getNextAndLastPrayerTimesRemotely(
                    getDayCounter(dayCounter),
                    currentLocation.longitude.toString(),
                    currentLocation.longitude.toString()
                )
            }
            binding.dateDailyTV.text = convertDateFormat(getDayCounter(dayCounter))
        } else Toast.makeText(
            requireContext(),
            "Please Check Your Connection",
            Toast.LENGTH_LONG
        ).show()
    }


    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                121
            )
        }
    }

    private fun initButtons(view: View){
        binding.dateDailyTV.text = getSystemDate()
        binding.qiblaBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToQiblaFragment(
                myAddress,
                getSystemDate(),
                currentLocation.latitude.toFloat(),
                currentLocation.longitude.toFloat(),
                currentLocation.altitude.toFloat()
            )
            Navigation.findNavController(view).navigate(action)
        }
        binding.forwardBtn.setOnClickListener {
            getDayTimings("++")
        }
        binding.backBtn.setOnClickListener {
            getDayTimings("--")
        }
    }

}