package com.example.wifimvvm.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wifimvvm.R
import com.example.wifimvvm.WifiAdapter
import com.example.wifimvvm.databinding.FragmentWifiBinding
import com.example.wifimvvm.repository.WifiHandlerRepository
import com.example.wifimvvm.utils.WifiHandler
import com.example.wifimvvm.viewModel.WifiHandlerViewModel
import com.example.wifimvvm.viewModel.WifiHandlerViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class WifiFragment : Fragment() {
    lateinit var binding: FragmentWifiBinding
    lateinit var wifiHandlerViewModel:WifiHandlerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi, container, false)

        binding.rvAvailableWifi.layoutManager = LinearLayoutManager(context as Activity)

        val bandWidth = arguments?.getString("band_width")

        val wifiHandler = WifiHandler(context as Activity, bandWidth!!)
        val repository =WifiHandlerRepository(wifiHandler)
        // Initialize ViewModel with ViewModelProvider
        wifiHandlerViewModel = ViewModelProvider(this, WifiHandlerViewModelFactory(repository))
            .get(WifiHandlerViewModel::class.java)

        // Set up the adapter
        val adapter = WifiAdapter { selectedWifi ->
            // Handle the selected WiFi
            showInputDialog(selectedWifi.SSID)
        }

        // Observe LiveData from the ViewModel
        wifiHandlerViewModel.wifiResults.observe(viewLifecycleOwner, Observer { wifiList ->
            Log.d("UpdatedWifiList", wifiList.toString())
            adapter.submitList(wifiList)
        })

        startWifiScanWithTimeout()

        // Start the WiFi scan
        wifiHandlerViewModel.startScan()

        binding.rvAvailableWifi.adapter  = adapter

        binding.pullToRefresh.setOnRefreshListener {
            startWifiScanWithTimeout()        }

        binding.pullToRefresh.isRefreshing = false

        return binding.root;
    }

    private fun showInputDialog(ssid: String) {
        val editText = EditText(context)

        val dialog = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Enter Password")
                .setMessage("Please provide a Wi-Fi password for $ssid:")
                .setView(editText)
                .setPositiveButton("OK") { dialog, which ->
                    val userInput = editText.text.toString().trim()
                    val fragment = WifiQrCodeFragment()
                    val bundle = Bundle().apply {
                        putString("WI_SSID", ssid)
                        putString("WI_PWD", userInput)
                    }
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_wifi, fragment)
                        .addToBackStack(null)
                        .commit()

                }
                .setNegativeButton("Cancel", null)
                .create()
        }

        if (dialog != null) {
            dialog.show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        wifiHandlerViewModel.stopScan()
    }

    private fun startWifiScanWithTimeout() {
        // Start the scan and ensure it stops after a certain time (e.g., 5 seconds)
        binding.pullToRefresh.isRefreshing = true // Start the refresh animation
        wifiHandlerViewModel.startScan()

        // Stop the scan after a delay (e.g., 5 seconds)
        binding.rvAvailableWifi.postDelayed({
            binding.pullToRefresh.isRefreshing = false // Stop the refresh animation after the delay
        }, 5000) // Delay of 5 seconds
    }
}