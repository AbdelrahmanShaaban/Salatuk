package com.example.a5alaty.ui.fragments

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.a5alaty.R
import com.example.a5alaty.databinding.FragmentQiblaBinding
import com.example.a5alaty.utils.CompassQibla
import java.util.Locale

class QiblaFragment : Fragment() {

    lateinit var binding: FragmentQiblaBinding
    private var currentCompassDegree = 0f
    private var currentNeedleDegree = 0f
    private val qiblaArgs: QiblaFragmentArgs by navArgs()
    private lateinit var myLocation: Location
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQiblaBinding.inflate(layoutInflater)
        initQibla()
        myLocation = Location(LocationManager.GPS_PROVIDER)
        myLocation.latitude = qiblaArgs.latArgs.toDouble()
        myLocation.longitude = qiblaArgs.longArgs.toDouble()
        myLocation.altitude = qiblaArgs.altituedArgs.toDouble()

        binding.showQiblaBtn.setOnClickListener {
            qiblaDirectionOnMap()
        }
        return binding.root

    }

    private fun initQibla(){
        CompassQibla.Builder(requireActivity() as AppCompatActivity).onPermissionGranted { permission ->
            Toast.makeText(requireContext(), "onPermissionGranted $permission", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            Toast.makeText(requireContext(), "onPermissionDenied", Toast.LENGTH_SHORT).show()
        }.onGetLocationAddress { address ->
            binding.tvLocation.text = buildString {
                append(qiblaArgs.addressArgs)
            }
        }.onDirectionChangeListener { qiblaDirection ->
            binding.tvDirection.text = if (qiblaDirection.isFacingQibla) "You're Facing Qibla"
            else "${qiblaDirection.needleAngle.toInt()}°"

            val rotateCompass = RotateAnimation(
                currentCompassDegree, qiblaDirection.compassAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentCompassDegree = qiblaDirection.compassAngle

            binding.ivCompass.startAnimation(rotateCompass)

            val rotateNeedle = RotateAnimation(
                currentNeedleDegree, qiblaDirection.needleAngle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
            }
            currentNeedleDegree = qiblaDirection.needleAngle

            binding.ivNeedle.startAnimation(rotateNeedle)
        }.build()
    }

    private fun qiblaDirectionOnMap() {

        val uri = String.format(
            Locale.ENGLISH,
            "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
            myLocation.latitude,
            myLocation.longitude,
            resources.getString(R.string.my_location),
            21.422487,
            39.826206,
            resources.getString(R.string.qibla)
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}