package com.practice.securewifi.check.wifi_points_selection.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.check.wifi_points_selection.model.WifiPointItem

class WifiesSelectionDiffItemCallback : DiffUtil.ItemCallback<WifiPointItem>() {
    override fun areItemsTheSame(
        oldItem: WifiPointItem,
        newItem: WifiPointItem
    ): Boolean {
        return (oldItem.ssid == newItem.ssid)
    }

    override fun areContentsTheSame(
        oldItem: WifiPointItem,
        newItem: WifiPointItem
    ): Boolean {
        return (oldItem == newItem)
    }
}