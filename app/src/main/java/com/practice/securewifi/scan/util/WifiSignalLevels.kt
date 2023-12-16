package com.practice.securewifi.scan.util

import com.practice.securewifi.R

object WifiSignalLevels {
    fun getImageResourceForSignalLevel(signalLevel: Int): Int {
        return when(signalLevel) {
            0 -> {
                R.drawable.signal_wifi_0_bar
            }
            1 -> {
                R.drawable.signal_wifi_1_bar
            }
            2 -> {
                R.drawable.signal_wifi_2_bar
            }
            3 -> {
                R.drawable.signal_wifi_3_bar
            }
            4 -> {
                R.drawable.signal_wifi_4_bar
            }
            5 -> {
                R.drawable.signal_wifi_5_bar
            }
            else -> {
                return 0
            }
        }
    }
}