package com.example.wifimvvm.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.net.wifi.ScanResult
import com.example.wifimvvm.utils.WifiHandler

class WifiHandlerRepository(private val wifiHandler: WifiHandler) {

    val wifiResultsLiveData: LiveData<List<ScanResult>> get() = wifiHandler.wifiResultsLiveData

    // Start the scan through the WifiHandler
    fun startWifiScan() {
        wifiHandler.startWifiScan()
    }

    fun stopWifiScan() {
        wifiHandler.stopWifiScan()
    }

}
