package com.example.wifimvvm.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.wifimvvm.R
import com.example.wifimvvm.databinding.FragmentWifiQrCodeBinding
import com.example.wifimvvm.utils.QrCodeHandler


class WifiQrCodeFragment : Fragment() {
    lateinit var    binding:FragmentWifiQrCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_qr_code, container, false)

        val wifiSSID =arguments?.getString("WI_SSID")
        val wifiPassword = arguments?.getString("WI_PWD")
        val wifiType = "WPA" // Could be "WEP" or leave empty for open network

        val qrCodeString = "WIFI:S:$wifiSSID;T:$wifiType;P:$wifiPassword;;"
        val qrCode = QrCodeHandler().generateQrCode(qrCodeString)

        binding.qrCodeImageView.setImageBitmap(qrCode)  
        binding.wifissid.text = wifiSSID
        binding.wifipwd.text = wifiPassword
        return binding.root
    }


}