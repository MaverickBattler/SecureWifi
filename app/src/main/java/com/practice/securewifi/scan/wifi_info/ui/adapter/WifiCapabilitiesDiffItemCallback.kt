package com.practice.securewifi.scan.wifi_info.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.scan.wifi_info.model.WifiCapabilityItem

class WifiCapabilitiesDiffItemCallback : DiffUtil.ItemCallback<WifiCapabilityItem>() {
    override fun areItemsTheSame(oldItem: WifiCapabilityItem, newItem: WifiCapabilityItem): Boolean =
        (oldItem.shortName == newItem.shortName)

    override fun areContentsTheSame(oldItem: WifiCapabilityItem, newItem: WifiCapabilityItem): Boolean =
        (oldItem == newItem)
}