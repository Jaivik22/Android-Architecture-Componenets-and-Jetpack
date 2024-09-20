package com.example.wifimvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import android.net.wifi.ScanResult
import com.example.wifimvvm.repository.WifiHandlerRepository

class WifiHandlerViewModel(private val wifiHandlerRepository: WifiHandlerRepository) : ViewModel() {

    val wifiResults: LiveData<List<ScanResult>> = wifiHandlerRepository.wifiResultsLiveData

    // Start and stop scan methods
    fun startScan() {
        wifiHandlerRepository.startWifiScan()
    }

    fun stopScan() {
        wifiHandlerRepository.stopWifiScan()
    }
}
