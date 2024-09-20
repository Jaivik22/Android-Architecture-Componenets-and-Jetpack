package com.example.wifimvvm

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.karumi.dexter.Dexter

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wifimvvm.databinding.ActivityMainBinding
import com.example.wifimvvm.fragments.WifiFragment
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // Handle the permissions granted or denied case
                    if (report.areAllPermissionsGranted()) {
                        // All permissions are granted
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // Handle permanent denial of any permission
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    // Show rationale to the user for requesting permissions
                    token?.continuePermissionRequest()
                }
            }).check()

        binding.lowerBtn.setOnClickListener {
            if(checkAndPromptLocation(this)) {

                Log.d("Click", "click")
                val fragment = WifiFragment()
                val bundle = Bundle().apply {
                    putString("band_width", "lower")
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.higherBtn.setOnClickListener {
            if(checkAndPromptLocation(this)) {

                Log.d("Click", "click")
                val fragment = WifiFragment()
                val bundle = Bundle().apply {
                    putString("band_width", "higher")
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    fun checkAndPromptLocation(context: Context) : Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!gpsEnabled && !networkEnabled) {
            // Prompt the user to enable location services
            Toast.makeText(context, "Location is off, please enable it.", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
            return false
        } else {
            Toast.makeText(context, "Location is on.", Toast.LENGTH_SHORT).show()
            return true
        }
    }
}