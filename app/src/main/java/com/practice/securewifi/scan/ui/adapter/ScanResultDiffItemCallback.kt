package com.practice.securewifi.scan.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.scan.model.WifiScanResult

class ScanResultDiffItemCallback : DiffUtil.ItemCallback<WifiScanResult>() {
    override fun areItemsTheSame(oldItem: WifiScanResult, newItem: WifiScanResult): Boolean =
        (oldItem.ssid == newItem.ssid)

    override fun areContentsTheSame(oldItem: WifiScanResult, newItem: WifiScanResult): Boolean =
        (oldItem == newItem)
}