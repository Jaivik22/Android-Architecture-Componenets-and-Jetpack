package com.example.wifimvvm

import android.annotation.SuppressLint
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wifimvvm.R


class WifiAdapter(
    private val onItemClick: (ScanResult) -> Unit // Callback function for item click
) : ListAdapter<ScanResult, WifiAdapter.WifiViewHolder>(WifiDiffCallback()) {

    // ViewHolder class to hold item view references
    class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ssidTextView: TextView = itemView.findViewById(R.id.ssidTextView)
        val wifiImg: ImageView = itemView.findViewById(R.id.wifiImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wifi, parent, false)
        return WifiViewHolder(view)
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {
        val wifi = getItem(position)
        holder.ssidTextView.text = wifi.SSID// Set the SSID of the WiFi network

        if (!isProtectedNetwork(wifi.capabilities)) {
            holder.wifiImg.setImageResource(R.drawable.wifiopen)
        } else {
            holder.wifiImg.setImageResource(R.drawable.wifilock)
        }

        // Set click listener on the itemView
        holder.itemView.setOnClickListener {
            onItemClick(wifi) // Trigger the callback with the clicked item
        }
    }

    // DiffUtil.Callback implementation to handle list differences
    class WifiDiffCallback : DiffUtil.ItemCallback<ScanResult>() {
        override fun areItemsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
            // Compare by SSID or another unique identifier if available
            return oldItem.SSID == newItem.SSID
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
            // Compare contents of the items
            return oldItem == newItem
        }
    }

    private fun isProtectedNetwork(capability: String): Boolean {
        return (capability.contains("WPA") ||
                capability.contains("WEP") ||
                capability.contains("WPS"))
    }
}
