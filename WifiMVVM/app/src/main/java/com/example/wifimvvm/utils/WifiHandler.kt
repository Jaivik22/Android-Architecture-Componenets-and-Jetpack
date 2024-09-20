package com.example.wifimvvm.utils

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WifiHandler(private val context: Context,private val bandWidth: String) {
    private var wifiManager: WifiManager
    private var wifiPollingJob: Job? = null
    private val wifiList: MutableList<ScanResult> = mutableListOf()
    private var wifiScanCallback: ((List<ScanResult>) -> Unit)? = null  // Callback
    private val _wifiResultsLiveData = MutableLiveData<List<ScanResult>>()
    val wifiResultsLiveData: LiveData<List<ScanResult>> get() = _wifiResultsLiveData


    init {
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        registerReceiver()  // Register the BroadcastReceiver
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)
    }

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("WifiReceiver", "Received scan results broadcast")
            val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false) ?: false
            if (success) {
                Log.d("WifiReceiver", "Scan success")
                handleScanSuccess()
            } else {
                Log.d("WifiReceiver", "Scan failure")
                handleScanFailure()
            }
        }
    }

    private fun handleScanSuccess() {
        if (hasLocationPermission()) {
            try {
                val results = wifiManager.scanResults
                wifiList.clear()
                wifiList.addAll(results.filter { it.frequency in 2400..2500 })
                Log.d("WifiHandler", wifiList.toString())

                wifiScanCallback?.invoke(wifiList)  // Invoke the callback with results
            } catch (e: SecurityException) {
                Log.e("WifiHandler", "Permission denied while accessing scan results", e)
            }
        } else {
            Log.e("WifiHandler", "Location permission not granted")
        }
    }

    private fun handleScanFailure() {
        if (hasLocationPermission()) {
            try {
                val results = wifiManager.scanResults
                wifiList.clear()
                wifiList.addAll(results.filter { it.frequency in 2400..2500 })
                Log.d("WifiHandler", wifiList.toString())
                wifiScanCallback?.invoke(wifiList)  // Invoke the callback with fallback results
            } catch (e: SecurityException) {
                Log.e("WifiHandler", "Permission denied while accessing scan results", e)
            }
        } else {
            Log.e("WifiHandler", "Location permission not granted")
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun startWifiScan(){
        // Set the callback
//        wifiScanCallback = callback

//        if (!hasLocationPermission()) {
//            Log.e("WifiHandler", "Location permission not granted")
//            return
//        }

        wifiPollingJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    wifiManager.startScan()
                    delay(2000)
                    val results = wifiManager.scanResults
                    val uniqueWifiList = results
                        .filter { it.SSID.isNotEmpty() } // Remove blank SSIDs
                        .distinctBy { it.BSSID }

                    val filteredList = if (bandWidth.equals("lower")) {
                        // Filter for 2400-2500 MHz range
                        uniqueWifiList.filter { it.frequency in 2400..2500 }
                    } else {
                        // Filter for 5000-6000 MHz range
                        uniqueWifiList.filter { it.frequency in 5000..6000 }
                    }
                    withContext(Dispatchers.Main) {
                        _wifiResultsLiveData.value = filteredList
                    }
                    Log.e("WifiManager",wifiList.toString())
                    delay(5000)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    Log.e("WifiManager",e.printStackTrace().toString())
                }
            }
        }
    }

    fun stopWifiScan() {
        wifiPollingJob?.cancel()
        try {
            context.unregisterReceiver(wifiScanReceiver)

        }catch (e:Exception){
            println(e.message.toString())
        }
    }
}
