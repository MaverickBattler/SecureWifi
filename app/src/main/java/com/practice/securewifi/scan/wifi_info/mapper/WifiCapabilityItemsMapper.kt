package com.practice.securewifi.scan.wifi_info.mapper

import android.app.Application
import android.net.wifi.ScanResult
import com.practice.securewifi.R
import com.practice.securewifi.scan.wifi_info.model.WifiCapabilityItem
import com.practice.securewifi.scan.wifi_info.model.WifiInfoUiState

class WifiCapabilityItemsMapper(
    private val application: Application
) {

    fun map(scanResult: ScanResult, areThereResultsForWifi: Boolean): WifiInfoUiState {
        val wifiSsid = scanResult.SSID
        val wifiCapabilitiesItems = getWifiCapabilitiesItems(scanResult)
        return WifiInfoUiState.Content(
            wifiSsid = wifiSsid,
            wifiCapabilities = wifiCapabilitiesItems,
            buttonCheckResultsVisible = areThereResultsForWifi
        )
    }

    private fun getWifiCapabilitiesItems(scanResult: ScanResult): List<WifiCapabilityItem> {
        val wifiCapabilityItems = mutableListOf<WifiCapabilityItem>()
        if (scanResult.isPasspointNetwork) {
            wifiCapabilityItems.add(
                WifiCapabilityItem(
                    shortName = "Passpoint Network",
                    shortExplanation = application.getString(R.string.passpoint_short_explanation),
                    explanation = application.getString(R.string.passpoint_explanation),
                )
            )
        }
        val capabilitiesString = scanResult.capabilities
        val capabilitiesNames = capabilitiesString.split("[", "]", "-").distinct()
        capabilitiesNames.forEach { capabilityName ->
            getWifiCapabilityForCapabilityName(capabilityName)?.let { wifiCapabilityItem ->
                wifiCapabilityItems.add(wifiCapabilityItem)
            }
        }
        return wifiCapabilityItems
    }

    private fun getWifiCapabilityForCapabilityName(capabilityName: String): WifiCapabilityItem? {
        val shortExplanation: String
        val explanation: String
        when (capabilityName) {
            "WPA" -> {
                shortExplanation = application.getString(R.string.wpa_short_explanation)
                explanation = application.getString(R.string.wpa_explanation)
            }

            "WPA2" -> {
                shortExplanation = application.getString(R.string.wpa2_short_explanation)
                explanation = application.getString(R.string.wpa2_explanation)
            }

            "WEP" -> {
                shortExplanation = application.getString(R.string.wep_short_explanation)
                explanation = application.getString(R.string.wep_explanation)
            }

            "ESS" -> {
                shortExplanation = application.getString(R.string.ess_short_explanation)
                explanation = application.getString(R.string.ess_explanation)
            }

            "IBSS" -> {
                shortExplanation = application.getString(R.string.ibss_short_explanation)
                explanation = application.getString(R.string.ibss_explanation)
            }

            "WPS" -> {
                shortExplanation = application.getString(R.string.wps_short_explanation)
                explanation = application.getString(R.string.wps_explanation)
            }

            "RSN" -> {
                shortExplanation = application.getString(R.string.rsn_short_explanation)
                explanation = application.getString(R.string.rsn_explanation)
            }

            "PSK" -> {
                shortExplanation = application.getString(R.string.psk_short_explanation)
                explanation = application.getString(R.string.psk_explanation)
            }

            "EAP" -> {
                shortExplanation = application.getString(R.string.eap_short_explanation)
                explanation = application.getString(R.string.eap_explanation)
            }

            "TKIP" -> {
                shortExplanation = application.getString(R.string.tkip_short_explanation)
                explanation = application.getString(R.string.tkip_explanation)
            }

            "CCMP" -> {
                shortExplanation = application.getString(R.string.ccmp_short_explanation)
                explanation = application.getString(R.string.ccmp_explanation)
            }

            "OPEN" -> {
                shortExplanation = application.getString(R.string.open_short_explanation)
                explanation = application.getString(R.string.open_explanation)
            }

            "IEEE8021X" -> {
                shortExplanation = application.getString(R.string.ieee8021x_short_explanation)
                explanation = application.getString(R.string.ieee8021x_explanation)
            }

            else -> {
                return null
            }
        }
        return WifiCapabilityItem(
            shortName = capabilityName,
            shortExplanation = shortExplanation,
            explanation = explanation,
        )
    }
}