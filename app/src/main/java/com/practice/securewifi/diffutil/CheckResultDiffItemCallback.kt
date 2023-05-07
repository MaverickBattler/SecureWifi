package com.practice.securewifi.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.domain.display.DisplayWifiCheckResult

class CheckResultDiffItemCallback : DiffUtil.ItemCallback<DisplayWifiCheckResult>() {
    override fun areItemsTheSame(
        oldItem: DisplayWifiCheckResult,
        newItem: DisplayWifiCheckResult
    ): Boolean =
        (oldItem.ssid == newItem.ssid)

    override fun areContentsTheSame(
        oldItem: DisplayWifiCheckResult,
        newItem: DisplayWifiCheckResult
    ): Boolean =
        (oldItem == newItem)
}