package com.practice.securewifi.scan

import androidx.recyclerview.widget.DiffUtil

class ScanResultDiffItemCallback : DiffUtil.ItemCallback<WifiScanResult>() {
    override fun areItemsTheSame(oldItem: WifiScanResult, newItem: WifiScanResult): Boolean =
        (oldItem.ssid == newItem.ssid)

    override fun areContentsTheSame(oldItem: WifiScanResult, newItem: WifiScanResult): Boolean =
        (oldItem == newItem)
}