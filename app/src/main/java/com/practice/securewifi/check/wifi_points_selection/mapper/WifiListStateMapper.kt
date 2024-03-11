package com.practice.securewifi.check.wifi_points_selection.mapper

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import com.practice.securewifi.check.wifi_points_selection.model.WifiListState
import com.practice.securewifi.check.wifi_points_selection.model.WifiPointItem

class WifiListStateMapper {

    fun map(
        scanResults: List<ScanResult>,
        selectedWifiesSsids: List<String>
    ): WifiListState {

        if (scanResults.isEmpty()) {
            return WifiListState.Loading
        }

        val nearbyWifies = scanResults
            .map { scanResult ->
            val ssid = scanResult.SSID
            val signalLevel = WifiManager.calculateSignalLevel(scanResult.level, 6)
            val selected = selectedWifiesSsids.contains(scanResult.SSID)
            WifiPointItem(
                ssid = ssid,
                signalLevel = signalLevel,
                selected = selected
            )
        }
        val selectedWifiesNotInScanResults = selectedWifiesSsids.filter { selectedWifiSsid ->
            scanResults.find { scanResult -> scanResult.SSID == selectedWifiSsid } == null
        }.map { selectedWifiSsid ->
            val ssid = selectedWifiSsid
            val signalLevel = WifiManager.calculateSignalLevel(0, 6)
            val selected = true
            WifiPointItem(
                ssid = ssid,
                signalLevel = signalLevel,
                selected = selected
            )
        }
        val wifiPointItems = nearbyWifies + selectedWifiesNotInScanResults
        return if (wifiPointItems.isEmpty()) {
            WifiListState.Loading
        } else {
            WifiListState.WifiList(wifiPointItems)
        }
    }
}