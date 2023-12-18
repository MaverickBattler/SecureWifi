package com.practice.securewifi.check_results.adapter

import androidx.recyclerview.widget.DiffUtil
import com.practice.securewifi.check_results.model.DisplayWifiCheckResult

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