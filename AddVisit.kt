package com.satmatgroup.satmatmr

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.satmatgroup.satmatmr.databinding.ActivityAddVisitBinding
import java.util.Locale

class AddVisit : AppCompatActivity() {
    private lateinit var binding: ActivityAddVisitBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myAddress = ""

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVisitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.getLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // Request the permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST
                )
            }
            else{
                // Permission has already been granted
                getLocation()
            }
        }



    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null)
            {
                getCityName(location.latitude, location.longitude)
                binding.addtess.text = myAddress
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
        }
    }

    private fun getCityName(lat:Double, long:Double){

        try {

            val geoCoder = Geocoder(this, Locale.getDefault())
            val address = geoCoder.getFromLocation(lat, long,3)
            if (address != null)
            {
                myAddress = address[0].getAddressLine(0)
            }

        } catch (e: Exception)
        {
            Toast.makeText(this,"Loading city", Toast.LENGTH_SHORT).show()
        }

    }

}
